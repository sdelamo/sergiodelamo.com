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
import io.micronaut.tags.Tag
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
        Map<String, String> m = RenderSiteTask.siteMeta(title.get(), about.get(), url.get(), keywords.get() as List<String>, robots.get())
        copyBackgroundImages()
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

    void copyBackgroundImages() {
        File images = new File(assets.get().absolutePath + "/" + "bgimages")
        File outputImages = new File(dist().absolutePath + '/images')
        outputImages.mkdir()
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(images)
                copySpec.into(outputImages)
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
    static String renderPostHtml(HtmlPost htmlPost,
                                 String templateText,
                                 List<HtmlPost> posts) {

        StringWriter writer = new StringWriter()
        MarkupBuilder mb = new MarkupBuilder(writer)
        mb.div {
            mkp.yieldUnescaped(htmlPost.html)
        }
        mb.p {
            if (htmlPost.metadata['keywords']) {
                mkp.yield('Tags: ')
                for (String tag : htmlPost.metadata['keywords'].split(',')) {
                    a(href: "./tag/${tag}.html") {
                        mkp.yield("#${tag}")
                    }
                }
            }
            br {

            }
            if (htmlPost.metadata['date_published']) {
                span(class: "date") {
                    mkp.yield(YYYY_MM_DD_FORMAT.format(JSON_FEED_FORMAT.parse(htmlPost.metadata['date_published'] as String)))
                    mkp.yield('.')
                }
            }
            if (htmlPost.metadata['author.name']) {
                span(class: "author") {
                    mkp.yield(htmlPost.metadata['author.name'])
                }
            }
        }
        String html = writer.toString()
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

    static List<HtmlPost> processPosts(Map<String, String> globalMetadata, List<MarkdownPost> markdownPosts) {
        markdownPosts.collect { MarkdownPost mdPost ->
                Map<String, String> metadata = RenderSiteTask.processMetadata(globalMetadata + mdPost.metadata)
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

            Set<String> postTags = parseTags(contentHtml)
            new HtmlPost(metadata: postMetadata, html: contentHtml, path: mdPost.path, tags: postTags)
        }
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
            for (String postTag : postTags) {
                tagsMap[postTag] = tagsMap.containsKey(postTag) ? (1 + tagsMap[postTag]) : 1
                if (!tagPosts.containsKey(postTag)) {
                    tagPosts[postTag] = []
                }
                tagPosts[postTag] << htmlPost.path
            }
            String postLink = postLink(htmlPost)
            String uuid = htmlPost.path.replace(".html", "")
            String cleanupHtml = htmlWithoutTitleAndDate(htmlPost.html)
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
            if (htmlPost.metadata['author.name'] || htmlPost.metadata['author.url'] || htmlPost.metadata['author.avatar']) {
                JsonFeedAuthor.Builder authorBuilder = JsonFeedAuthor.builder()
                if (htmlPost.metadata['author.name']) {
                    authorBuilder = authorBuilder.name(htmlPost.metadata['author.name'] as String)
                }
                if (htmlPost.metadata['author.url']) {
                    authorBuilder = authorBuilder.url(htmlPost.metadata['author.url'] as String)
                }
                if (htmlPost.metadata['author.avatar']) {
                    authorBuilder = authorBuilder.avatar(htmlPost.metadata['author.avatar'] as String)
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
        Set<Tag> tags = tagsMap.collect { k, v -> new Tag(title: k, ocurrence: v) } as Set<Tag>
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
        renderArchive(new File(outputDir.getAbsolutePath() + "/" + "index.html"), listOfPosts, templateText, globalMetadata, "Archive")
    }

    static String toRFC3339(Date d) {
        JSON_FEED_FORMAT.format(d)
    }

    static void renderArchive(File output,
                       List<HtmlPost> posts,
                       String templateText,
                       Map<String, String> metadata,
                       String title) {
        String html = "<h1>${title}</h1>"

        html += posts.collect { post ->
            "<article class='post'><h2><a href=\"${post.metadata['url']}/blog/${post.path}\">${post.metadata['title']}</a></h2><p>${YYYY_MM_DD_FORMAT.format(JSON_FEED_FORMAT.parse(post.metadata['date_published'] as String))} - ${post.metadata['summary']}</p></article>"
        }.join("\n")

        Map<String, String> m = new HashMap<>(metadata)
        m = RenderSiteTask.processMetadata(m)
        m[ROBOTS] = [ROBOTS_NOINDEX, ROBOTS_FOLLOW].join(', ')
        String renderedHtml = RenderSiteTask.renderHtmlWithTemplateContent(html, m, templateText)
        output.createNewFile()
        output.text = renderedHtml
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
                        .avatar('https://images.sergiodelamo.com/smallavatar.png') 
                        .build())
                .icon('https://images.sergiodelamo.com/smallavatar.png')
                .favicon('https://images.sergiodelamo.com/favicon.jpg')
                .language("en")
                .homePageUrl(sitemeta.url)
                .feedUrl("${sitemeta.url}/feed.json")
                .items(items)
                .build()
        outputFile.createNewFile()
        outputFile.text = new JsonBuilder(jsonFeed.toMap()).toPrettyString()
    }

    private static void renderRss(Map<String, String> sitemeta, List<RssItem> rssItems, File outputFile) {
        RssChannel.Builder builder = RssChannel.builder(sitemeta['title'], sitemeta['url'], sitemeta['summary'])
        builder.pubDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
        builder.lastBuildDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
                .docs("http://blogs.law.harvard.edu/tech/rss")
                .generator("Micronaut RSS")
                .managingEditor("sergio.delamo@softamo.com")
                .webMaster("sergio.delamo@softamo.com")
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
