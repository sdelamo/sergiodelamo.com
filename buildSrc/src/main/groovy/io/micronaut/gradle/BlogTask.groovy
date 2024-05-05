package io.micronaut.gradle

import groovy.time.TimeCategory
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.ContentAndMetadata
import io.micronaut.HtmlPost
import io.micronaut.MarkdownPost
import io.micronaut.MarkdownUtil
import io.micronaut.PostMetadata
import io.micronaut.PostMetadataAdapter
import io.micronaut.rss.DefaultRssFeedRenderer
import io.micronaut.rss.RssChannel
import io.micronaut.rss.RssFeedRenderer
import io.micronaut.rss.RssItem
import io.micronaut.rss.jsonfeed.JsonFeed
import io.micronaut.rss.jsonfeed.JsonFeedAuthor
import io.micronaut.rss.jsonfeed.JsonFeedItem
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.CopySpec
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.annotation.Nonnull
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.stream.Collectors
import groovy.json.JsonBuilder

@CompileStatic
class BlogTask extends DefaultTask {

    static DateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat("MMM yyyy, dd")
    static DateFormat JSON_FEED_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    static DateFormat GMT_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'");
    static {
        TimeZone tz = TimeZone.getTimeZone("Europe/Madrid")
        YYYY_MM_DD_FORMAT.setTimeZone(tz)
        JSON_FEED_FORMAT.setTimeZone(tz)
        GMT_FORMAT.setTimeZone(tz)
    }
    static final String H1_OPEN = '<h1>'
    static final String H1_CLOSE = '</h1>'    
    static final String SPAN_DATE_OPEN = '<span class="date">'
    public static final String RSS_FILE = 'rss.xml'
    public static final String JSONFEED_FILE = 'feed.json'
    public static final String IMAGES = 'images'
    final static String HASHTAG_SPAN = "<span class=\"hashtag\">#"
    final static String SPAN_CLOSE = "</span>"
    public static final int MAX_RELATED_POSTS = 2
    public static final String BLOG = 'blog'
    public static final String TAG = 'tag'
    public static final String INDEX = 'index.html'
    public static List<String> ALLOWED_TAG_PREFIXES = new ArrayList<>()
    public static final String ROBOTS = 'robots'
    public static final String ROBOTS_INDEX = 'index'
    public static final String ROBOTS_NOINDEX = 'noindex'
    public static final String ROBOTS_FOLLOW = 'follow'
    public static final String EVENTS_TAG = "[%events]"
    static {
        List<String> characters = 'A'..'Z'
        List<Integer> digits = [0,1,2,3,4,5,6,7,8,9]
        List<String> l = characters.stream()
                .map({str -> "#${str}".toString()})
                .collect(Collectors.toList())
        l.addAll(characters.stream()
                .map({str -> "#${str.toLowerCase()}".toString()})
                .collect(Collectors.toList()))
        l.addAll(digits.stream()
                .map({digit -> "#${digit}".toString()})
                .collect(Collectors.toList()))
        ALLOWED_TAG_PREFIXES = l
    }

    @Input
    final Property<File> document = project.objects.property(File)

    @Input
    final Property<String> title = project.objects.property(String)

    @Input
    final Property<String> email = project.objects.property(String)

    @Input
    final Property<String> authorName = project.objects.property(String)

    @Input
    final Property<String> authorUrl = project.objects.property(String)

    @Input
    final Property<String> authorAvatar = project.objects.property(String)

    @Input
    final Property<String> about = project.objects.property(String)

    @Input
    final Property<String> url = project.objects.property(String)

    @Input
    final ListProperty<String> keywords = project.objects.listProperty(String)

    @Input
    final Property<String> robots = project.objects.property(String)

    @InputDirectory
    final Property<File> posts = project.objects.property(File)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @InputDirectory
    final Property<File> assets = project.objects.property(File)

    File dist() {
        new File(output.get().absolutePath + "/" + RenderSiteTask.DIST)
    }

    @TaskAction
    void renderBlog() {
        File template = document.get()
        final String templateText = template.text
        File o = dist()
        Map<String, String> m = RenderSiteTask.siteMeta(title.get(),
                about.get(),
                url.get(),
                keywords.get() as List<String>,
                robots.get(),
                email.get(),
                authorName.get(),
                authorUrl.get(),
                authorAvatar.get())
        List<MarkdownPost> listOfPosts = parsePosts(posts.get())
        listOfPosts = filterOutFuturePosts(listOfPosts)
        listOfPosts = listOfPosts.sort { a, b ->
            parseDate(a.datePublished).after(parseDate(b.datePublished)) ? -1 : 1
        }
        List<HtmlPost> htmlPosts = processPosts(m, listOfPosts)
        File blog = new File(o.absolutePath + '/' + BLOG)
        blog.mkdir()
        renderPosts(m, htmlPosts, blog, templateText)
        copyBlogImages()
    }

    @CompileDynamic
    static List<MarkdownPost> filterOutFuturePosts(List<MarkdownPost> posts) {
        Date d = new Date()
        use(TimeCategory) {
            d += 1.day
        }
        posts.each { post ->
            if (post.datePublished == null) {
                throw new GradleException("date is null for ${post.path}")
            }
        }
        posts.findAll { post -> !parseDate(post.datePublished).after(d) }
    }

    static Date parseDate(String date) throws ParseException {
        if (!date) {
            throw new GradleException("Could not parse date $date")
        }
        try {
            return JSON_FEED_FORMAT.parse(date)
        } catch(ParseException e) {
            throw new GradleException("Could not parse date $date")
        }
    }

    void copyBlogImages() {
        File images = new File(posts.get().absolutePath)
        File outputBlogImages = new File(dist().absolutePath + '/' + BLOG)
        outputBlogImages.mkdir()
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(images)
                copySpec.into(outputBlogImages)
                copySpec.include(CopyAssetsTask.IMAGE_EXTENSIONS)
            }
        })
    }

    static String htmlWithoutTitleAndDate(String html) {        
        String result = removeElement(html, H1_OPEN, H1_CLOSE) 
        result = removeElement(result, '<p>By <span class="author">', SPAN_CLOSE + ' - ')        
        result = removeElement(result, SPAN_DATE_OPEN, SPAN_CLOSE + '</p>')        
        result
    }
    
    static String removeElement(String html, String opening, String close) {
        int start = html.indexOf(opening)
        int end = html.indexOf(close)        
        if (start == -1 || end == -1) {
            return html
        }
        String result = html.substring(0, start)
        //result += html.substring(start + opening.length(), end)
        result += html.substring(end + close.length())
        result
    }

    static RssItem rssItemWithPage(String title,
                                   Date pubDate,
                                   String link,
                                   String guid,
                                   String html,
                                   String author) {
        RssItem.Builder builder = RssItem.builder()
                .title(title)
                .pubDate(ZonedDateTime.of(Instant.ofEpochMilli(pubDate.time)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(), ZoneId.of("GMT")))
                .link(link)
                .guid(guid)
                .description(html)
        if (author) {
            builder = builder.author(parseAuthorName(author))
        }
        builder.build()
    }

    static String parseAuthorName(String author) {
        author.contains(" (") ? author.substring(0, author.indexOf(' (')) : author
    }

    @CompileDynamic
    static String backToTopFooter(String url) {
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.footer(class:  "py-5 text-center text-body-secondary bg-body-tertiary") {
            p(class: "mb-0") {
                a(href: url) {
                    mkp.yield("Back to top")

                }
            }
        }
        writer.toString()
    }

    @CompileDynamic
    static String renderPost(boolean  renderHeader,
                             String title,
                             String postLink,
                             String externalUrl,
                             Object datePublished,
                             String authorName,
                            String keywords,
                             String html) {
        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.article(class: "blog-post") {
            if (renderHeader) {
                h2(class: "display-6 link-body-emphasis mb-1") {
                    a(href: externalUrl ?: postLink) {
                        mkp.yield(title)
                    }
                }
                p(class: "blog-post-meta") {
                    if (datePublished) {
                        mkp.yield(YYYY_MM_DD_FORMAT.format(JSON_FEED_FORMAT.parse(datePublished as String)))
                        mkp.yield('.')
                    }
                    if (authorName) {
                        mkp.yield(" by " + authorName)
                    }
                    if (externalUrl) {
                        a(href: postLink) {
                            mkp.yield("Permalink")
                        }
                    }
                }
            }

            mkp.yieldUnescaped(html)


            if (keywords) {
                p {
                    mkp.yield('Tags: ')
                    for (String tag : keywords.split(',')) {
                        a(href: "./tag/${tag}.html") {
                            mkp.yield("#${tag}")
                        }
                    }
                }
            }
        }
        return writer.toString()
    }

    @CompileDynamic
    static String renderPostHtml(HtmlPost htmlPost,
                                 String templateText,
                                 List<HtmlPost> posts) {
        String html = renderPost(false, htmlPost.title, htmlPost.link, htmlPost.externalUrl, htmlPost.metadata['date_published'], htmlPost.metadata['author_name'] as String, htmlPost.metadata['keywords'], htmlPost.html)
        Map<String, String> metadata = htmlPost.metadata.toMap()
        if (!metadata['keywords']) {
            metadata['keywords'] = htmlPost.tags.join(',')
        }
        html = RenderSiteTask.renderHtmlWithTemplateContent(html, metadata, templateText)
        html = RenderSiteTask.highlightMenu(html, metadata, htmlPost.path)
        metadata['body'] = metadata['body'] ? metadata['body'] : 'post'
        if (metadata['body']) {
            html = html.replace("<body>", "<body class='${metadata['body']}'>")
        }
        html += RenderSiteTask.renderHtmlWithTemplateContent("", metadata, "<div class='wrapper'><div class='container'><hr/>\n\n" + EVENTS_TAG + "</div></div>")
        html
    }

    static List<HtmlPost> relatedPosts(HtmlPost htmlPost, List<HtmlPost> posts) {
        List<HtmlPost> relatedPosts = []
        for (String tag : htmlPost.tags) {
            for (HtmlPost p : posts) {
                if (p.tags.contains(tag) && p.path != htmlPost.path) {
                    List<String> paths = relatedPosts*.path
                    if (paths.contains(p.path)) {
                        continue
                    }
                    relatedPosts.add(p)
                    if (relatedPosts.size() > MAX_RELATED_POSTS) {
                        break
                    }
                }
            }
            if (relatedPosts.size() > MAX_RELATED_POSTS) {
                break
            }
        }
        if (relatedPosts.size() < MAX_RELATED_POSTS) {
            for (HtmlPost p : posts) {
                List<String> paths = relatedPosts*.path
                paths.add(htmlPost.path)
                if (paths.contains(p.path)) {
                    continue
                }
                relatedPosts << p
                if (relatedPosts.size() > MAX_RELATED_POSTS) {
                    break
                }
            }
        }
        relatedPosts.subList(0, MAX_RELATED_POSTS).sort { a, b ->
            parseDate(a.metadata.datePublished).after(parseDate(b.metadata.datePublished)) ? -1 : 1
        }
    }

    static String PRISM_CSS = "[%url]/stylesheets/prism.css"
    static String PRISM_JS = "[%url]/javascripts/prism.js"

    static boolean containsCodeSnippet(MarkdownPost mdPost) {
        mdPost.content.contains('```')
    }

    static enum Prism {
        JS,
        CSS
    }

    static String linkToPrism(String prepath, Prism prism) {
        switch (prism) {
            case Prism.CSS:
                return PRISM_CSS;
            case Prism.JS:
                return PRISM_JS;
        }
    }

    static void addCodeHighlighting(Map<String, String> meta, MarkdownPost mdPost) {
        String prepath = ''
        mdPost.getPath().split('/').length.times {
            prepath += '../'
        }
        addCodeHighlighting(meta, prepath)
    }

    static void addCodeHighlighting(Map<String, String> meta, String path) {
        meta['JAVASCRIPT'] = meta['JAVASCRIPT'] ? "${meta['JAVASCRIPT']},${linkToPrism(path, Prism.JS)}".toString() : linkToPrism(path, Prism.JS)
        meta['CSS'] = meta['CSS'] ? "${meta['CSS']},${linkToPrism(path, Prism.JS)}".toString() : linkToPrism(path, Prism.CSS)
    }

    static List<HtmlPost> processPosts(Map<String, String> globalMetadata, List<MarkdownPost> markdownPosts) {
        markdownPosts.collect { MarkdownPost mdPost ->
            Map<String, String> meta = globalMetadata + mdPost.metadata
            if (containsCodeSnippet(mdPost)) {
                addCodeHighlighting(meta, mdPost)
            }
            Map<String, String> metadata = RenderSiteTask.processMetadata(meta)
            PostMetadata postMetadata = new PostMetadataAdapter(metadata)
            String markdown = mdPost.content
            if (metadata.containsKey('slides')) {
                markdown = markdown + "\n\n[Slides](${metadata['slides']})\n\n"
            }
            if (metadata.containsKey('code')) {
                markdown = markdown + "\n\n[Code](${metadata['code']})\n\n"
            }
            if (metadata.containsKey('image')) {
                if (metadata.containsKey('external_url')) {
                    markdown = markdown + "[![](${metadata['image']})](${metadata['external_url']})\n\n"
                } else {
            markdown = markdown + "![](${metadata['image']})\n\n"        
                }
            
            }
            if (metadata.containsKey('external_url')) {
                markdown = markdown + "\n\n[Go to the linked site](${metadata['external_url']})\n\n"
            }


            String contentHtml = wrapTags(metadata, MarkdownUtil.htmlFromMarkdown(markdown))
            String iframe = RenderSiteTask.parseVideoIframe(metadata)
            if (iframe) {
                contentHtml = contentHtml + iframe
            }

            if (mdPost.title.contains('Micronaut®')) {
                contentHtml += "<p><small>Micronaut® is a registered trademark of Object Computing, Inc. Use is for referential purposes and does not imply any endorsement or affiliation with any third-party product. Unauthorized use is strictly prohibited.</small></p>"
            }

            Set<String> postTags = parseTags(contentHtml) + getTags(mdPost)
                        
            new HtmlPost(metadata: postMetadata, html: contentHtml, path: mdPost.path, tags: postTags)
        }
    }

    static Set<String> getTags(MarkdownPost mdPost) {
        if (mdPost.metadata.get('keywords')) {
            if (mdPost.metadata['keywords'] instanceof String) {
                return (((String) mdPost.metadata['keywords']).replaceAll(" ", "").split(',') as Set<String>)
            } else if (mdPost.metadata['keywords'] instanceof Collection) {
                return (mdPost.metadata['keywords'] as Set<String>)
            }
        }
        return [] as Set<String>
    }

    static void renderPosts(Map<String, String> globalMetadata,
                            List<HtmlPost> listOfPosts,
                            File outputDir,
                            final String templateText) {
        List<RssItem> rssItems = []
        List<JsonFeedItem> feedItems = []
        Map<String, List<String>> tagPosts = [:]
        Map<String, Integer> tagsMap = [:]

        for (HtmlPost htmlPost : listOfPosts) {
            String html = renderPostHtml(htmlPost, templateText, listOfPosts)
            File pageOutput = new File(outputDir.absolutePath + "/" + htmlPost.path)
            pageOutput.createNewFile()
            pageOutput.text = html

            Set<String> postTags = parseTags(html)
            postTags += htmlPost.tags
            for (String postTag : postTags) {
                tagsMap[postTag] = tagsMap.containsKey(postTag) ? (1 + tagsMap[postTag]) : 1
                if (!tagPosts.containsKey(postTag)) {
                    tagPosts[postTag] = []
                }
                tagPosts[postTag] << htmlPost.path
            }
            String postLink = postLink(htmlPost)
            String uuid = htmlPost.path.replace(".html", "")
            String cleanupHtml = indexPostHtml(htmlPost)
            cleanupHtml = cleanupHtml.replace('\n','')
            rssItems.add(rssItemWithPage(htmlPost.metadata.title,
                    parseDate(htmlPost.metadata.datePublished),
                    postLink,
                    uuid,
                    cleanupHtml,
                    htmlPost.metadata.author))
                    
            JsonFeedItem.Builder jsonFeedItemBuilder = JsonFeedItem.builder()
                    .title(htmlPost.metadata.title as String)
                    .datePublished(toRFC3339(parseDate(htmlPost.metadata.datePublished)))
                    .url(postLink)
                    .id(uuid)
                    .contentHtml(cleanupHtml)
            if (htmlPost.metadata['author_name'] || htmlPost.metadata['author_url'] || htmlPost.metadata['author_avatar']) {
                JsonFeedAuthor.Builder authorBuilder = JsonFeedAuthor.builder()
                if (htmlPost.metadata['author_name']) {
                    authorBuilder = authorBuilder.name(htmlPost.metadata['author_name'] as String)
                }
                if (htmlPost.metadata['author_url']) {
                    authorBuilder = authorBuilder.url(htmlPost.metadata['author_url'] as String)
                }
                if (htmlPost.metadata['author_avatar']) {
                    authorBuilder = authorBuilder.avatar(htmlPost.metadata['author_avatar'] as String)
                }
                jsonFeedItemBuilder.author(authorBuilder.build())
            }
            if (htmlPost.metadata['banner_image']) {
                jsonFeedItemBuilder = jsonFeedItemBuilder.bannerImage(htmlPost.metadata['banner_image'] as String)
            }
            if (htmlPost.metadata['external_url']) {
                jsonFeedItemBuilder = jsonFeedItemBuilder.externalUrl(htmlPost.metadata['external_url'] as String)
            }
            postTags.each { tag -> 
                jsonFeedItemBuilder = jsonFeedItemBuilder.tag(tag)
            } 
            feedItems.add(jsonFeedItemBuilder.build())
        }
        for (String tag : tagsMap.keySet()) {
            List<HtmlPost> postsTagged = listOfPosts.findAll { it.tags.contains(tag) }
            File tagFolder = new File("${outputDir.absolutePath}/tag/")
            if (!tagFolder.exists()) {
                tagFolder.mkdir()
            }
            File tagFile = new File("${outputDir.absolutePath}/tag/${tag}.html")
            tagFile.createNewFile()

            renderArchive(tagFile, postsTagged, templateText, globalMetadata, "Tag: $tag")
        }
        renderRss(globalMetadata, rssItems, new File(outputDir.absolutePath + "/../" + RSS_FILE))
        renderJsonFeed(globalMetadata, feedItems, new File(outputDir.absolutePath + "/../" + JSONFEED_FILE))
        renderArchive(new File(outputDir.getAbsolutePath() + "/" + "index.html"), listOfPosts, templateText, globalMetadata, "Blog")
        renderArchive(new File(outputDir.getAbsolutePath() + "/../" + "index.html"), listOfPosts, templateText, globalMetadata, null)
        renderTagIndex(new File(outputDir.getAbsolutePath() + "/tag/index.html"), tagsMap, templateText, globalMetadata, "Tags")
    }

    static String toRFC3339(Date d) {
        JSON_FEED_FORMAT.format(d)
    }

    static void renderTagIndex(File output,
                               Map<String, Integer> tagsMap,
                               String templateText,
                               Map<String, String> metadata,
                               String title) {
        String html = "<h1>${title}</h1>"

        Collection<String> popularTags = tagsMap.sort { e1, e2 -> e2.value <=> e1.value }*.key.take(10)
        html += tagsHtml('Popular', metadata['url'], popularTags )
        html += tagsHtml('A - Z', metadata['url'], tagsMap.keySet().sort())

        renderIndexPage(output, templateText, metadata, html)
    }

    static String tagsHtml(String h2, String url, Collection<String> tags) {
        String html = "<article class='post'>"
        html += "<header><h2>${h2}</h2><header>"
        html += "<ul class='list-group mb-5'>"
        for (String tag : tags) {
            html += "<li class='list-group-item'><a href=\"${url}/blog/tag/${tag}.html\">${tag}</a></li>"
        }
        html += "</ul>"
        html += "</article>"
        html
    }

    static void renderIndexPage(File output,
                                String templateText,
                                Map<String, String> metadata,
                                String html) {
        Map<String, String> m = new HashMap<>(metadata)
        addCodeHighlighting(m, "./")

        m = RenderSiteTask.processMetadata(m)
        m[ROBOTS] = [ROBOTS_NOINDEX, ROBOTS_FOLLOW].join(', ')
        String renderedHtml = RenderSiteTask.renderHtmlWithTemplateContent(html, m, templateText)
        output.createNewFile()
        output.text = renderedHtml
    }

    static void renderArchive(File output,
                       List<HtmlPost> posts,
                       String templateText,
                       Map<String, String> metadata,
                       String title) {
        String html = EVENTS_TAG
        int max = 20
        boolean archive = title != null
        html += title ? "<h1>${title}</h1>" : ""
        int count = 0
        html += !archive && posts.size() > max ? posts.subList(0, max).collect { post -> htmlForPost(count++, post, archive)}.join("\n") :
                posts.collect { post -> htmlForPost(count++, post, archive)}.join("\n")
        renderIndexPage(output, templateText, metadata, html)
    }

    static String htmlForPost(int count, HtmlPost post, boolean archive) {
        String html = archive
                ? MarkdownUtil.htmlFromMarkdown(post.metadata['summary'] as String)
                : indexPostHtml(post)
        html = removeGoToLinkedSite(html, post.externalUrl)
        return renderPost(true, post.title, post.link, post.externalUrl, post.metadata['date_published'], post.metadata['author_name'] as String, "",  html)
    }

    static String indexPostHtml(HtmlPost post) {
        String html = cleanupTrademarkNotice(
                removeSpeakerDeckScript(
                        removeIframes(
                                htmlWithoutTitleAndDate(post.html)
                        ), post.metadata["speakerdeck"] as String
                )
        )
        html = addMetadataLinks(post, html)
        html
    }

    static String addMetadataLinks(HtmlPost post, String html) {
        String result = html
        String externalUrl = post.metadata["external_url"] ?: post.metadata["speakerdeck"]
        String speakerDeckUrl = post.metadata["speakerdeck"]
        if (externalUrl && externalUrl.contains("youtube.com") && speakerDeckUrl) {
            result += "<p>"
            result += "<a href=\"${externalUrl}\">Video</a><br/>"
            result += "<a href=\"${speakerDeckUrl}\">Slides</a>>"
            result += "</p>"
        }
        result
    }

    static String cleanupTrademarkNotice(String html) {
        html.replace("<p><small>Micronaut® is a registered trademark of Object Computing, Inc. Use is for referential purposes and does not imply any endorsement or affiliation with any third-party product. Unauthorized use is strictly prohibited.</small></p>", "")
    }

    static String removeGoToLinkedSite(String html, String externalUrl) {
        externalUrl ? html.replace("<p><a href=\"${externalUrl}\">Go to the linked site</a></p>", "") : html
    }

    final static String OPENING_IFRAME = "<iframe"
    final static String CLOSING_IFRAME = "</iframe>"

    static String removeIframes(String html) {
        String result = html
        if (html.contains(OPENING_IFRAME) && html.contains(CLOSING_IFRAME)) {
            result = html.substring(0, html.indexOf(OPENING_IFRAME)) + html.substring(html.indexOf(CLOSING_IFRAME) + CLOSING_IFRAME.length())
        }
        if (result.contains(OPENING_IFRAME) && html.contains(CLOSING_IFRAME)) {
            return removeIframes(result)
        }
        result
    }

    final static String OPENING_SCRIPT_SPEAKER_DECK = "<script async class=\"speakerdeck-embed\" "
    final static String CLOSING_SCRIPT = "</script>"

    static String removeSpeakerDeckScript(String html, String url) {
        if (html.contains(OPENING_SCRIPT_SPEAKER_DECK) && html.contains(CLOSING_SCRIPT)) {
            return html.substring(0, html.indexOf(OPENING_SCRIPT_SPEAKER_DECK)) +
                    html.substring(html.indexOf(CLOSING_SCRIPT) + CLOSING_SCRIPT.length())
        }
        html
    }

    static Set<String> parseTags(String html) {
        String pageHtml = html
        Set<String> tags = []
        for (; ;) {
            if (!(pageHtml.contains(HASHTAG_SPAN) && pageHtml.contains(SPAN_CLOSE))) {
                return tags
            }
            pageHtml = pageHtml.substring(pageHtml.indexOf(HASHTAG_SPAN) + HASHTAG_SPAN.length())
            String tag = pageHtml.substring(0, pageHtml.indexOf(SPAN_CLOSE))
            tags << tag
            pageHtml = pageHtml.substring(pageHtml.indexOf(SPAN_CLOSE) + SPAN_CLOSE.length())
        }
    }

    static String postLink(HtmlPost post) {
        post.metadata.url + '/' + BLOG + '/' + post.path
    }

    private static void renderJsonFeed(Map<String, String> sitemeta, List<JsonFeedItem> items, File outputFile) {
        JsonFeed jsonFeed = JsonFeed.builder()
                .version(JsonFeed.VERSION_JSON_FEED_1_1)
                .title("Sergio del Amo's Blog")
                .description(sitemeta.summary)
                .author(JsonFeedAuthor.builder()
                        .url(sitemeta.url)
                        .name("Sergio del Amo")
                        .avatar('https://images.sergiodelamo.com/smallavatar.jpg') 
                        .build())
                .icon('https://images.sergiodelamo.com/smallavatar.jpg')
                .favicon('https://images.sergiodelamo.com/favicon.jpg')
                .language("en")
                .homePageUrl(sitemeta.url)
                .feedUrl("${sitemeta.url}/feed.json")
                .items(items)
                .build()
        outputFile.createNewFile()
        outputFile.text = new JsonBuilder(jsonFeed.toMap()).toPrettyString()
    }

    private static void renderRss(Map<String, String> sitemeta,
                                  List<RssItem> rssItems,
                                  File outputFile) {
        RssChannel.Builder builder = RssChannel.builder(sitemeta['title'], sitemeta['url'], sitemeta['summary'])
                .pubDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
                .lastBuildDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
                .docs("https://cyber.harvard.edu/rss/rss.html")
                .generator("Micronaut RSS")
                .managingEditor(sitemeta['email'])
                .webMaster(sitemeta['email'])

        for (RssItem item : rssItems) {
            builder.item(item)
        }
        FileWriter writer = new FileWriter(outputFile)
        RssFeedRenderer rssFeedRenderer = new DefaultRssFeedRenderer()
        rssFeedRenderer.render(writer, builder.build())
        writer.close()
    }

    static boolean isTag(String word) {
        ALLOWED_TAG_PREFIXES.any {word.startsWith(it) }
    }

    @Nonnull
    static String wrapTags(Map<String, String> metadata, @Nonnull String html) {
        html.split("\n")
                .collect { line ->
                    if (line.startsWith("<p>") && line.endsWith("</p>")) {
                        String lineWithoutParagraphs = line.replaceAll("<p>", "")
                                .replaceAll("</p>", "")
                        String[] words = lineWithoutParagraphs.split(" ")
                        lineWithoutParagraphs = words.collect { word ->
                            if (isTag(word)) {
                                String tag = word
                                if (word.contains("<")) {
                                    tag = word.substring(0, word.indexOf("<"))
                                }
                                return "<a href=\"${metadata['url']}/${BLOG}/${TAG}/${tag.replaceAll("#", "")}.html\"><span class=\"hashtag\">${tag}</span></a>".toString()
                            } else {
                                return word
                            }
                        }.join(" ")
                        return "<p>${lineWithoutParagraphs}</p>".toString()
                    } else {
                        line
                    }
                }.join('\n')
    }

    static List<MarkdownPost> parsePosts(File posts) {
        List<MarkdownPost> listOfPosts = []
        posts.eachFile { file ->
            if (file.path.endsWith(".md") || file.path.endsWith(".markdown")) {
                ContentAndMetadata contentAndMetadata = RenderSiteTask.parseFile(file)
                listOfPosts << new MarkdownPost(filename: file.name, content: contentAndMetadata.content, metadata: contentAndMetadata.metadata)
            }
        }
        listOfPosts
    }


}
