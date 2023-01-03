package io.micronaut.gradle

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.ContentAndMetadata
import io.micronaut.MarkdownUtil
import io.micronaut.Page
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.util.StringUtils
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.data.ParserException
import net.fortuna.ical4j.model.component.CalendarComponent
import net.fortuna.ical4j.model.component.VEvent
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat
import java.util.stream.Collectors

import static groovy.io.FileType.FILES

import javax.annotation.Nonnull
import javax.validation.constraints.NotNull

@CompileStatic
class RenderSiteTask extends DefaultTask {

    static final Comparator<VEvent> START_DATE_COMPARATOR = new Comparator<VEvent>() {
        @Override
        int compare(VEvent o1, VEvent o2) {
            return o1.getStartDate().getDate().compareTo(o2.getStartDate().getDate())
        }
    }

    public static final String YOUTUBE_WATCH = 'https://www.youtube.com/watch?v='
    static final String TWITER_HANDLE = '@sdelamo'
    static final String COLON = ":"
    static final String SEPARATOR = "---"
    public static final String DIST = "dist"
    public static final int TWITTER_CARD_PLAYER_WIDTH = 560
    public static final int TWITTER_CARD_PLAYER_HEIGHT = 315
    public static final String TEMP = "temp"
    public static final String ROBOTS = "robots"
    public static final String ICAL_NAME = "sdelamocalendar.ics"

    @InputDirectory
    final Property<File> pages = project.objects.property(File)

    @Input
    final Property<File> document = project.objects.property(File)

    @Input
    final Property<String> title = project.objects.property(String)

    @Input
    final Property<String> about = project.objects.property(String)

    @Input
    final Property<String> email = project.objects.property(String)

    @Input
    final Property<String> authorName = project.objects.property(String)

    @Input
    final Property<String> authorUrl = project.objects.property(String)

    @Input
    final Property<String> authorAvatar = project.objects.property(String)

    @Input
    final Property<String> url = project.objects.property(String)

    @Input
    final ListProperty<String> keywords = project.objects.listProperty(String)

    @Input
    final Property<String> robots = project.objects.property(String)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @TaskAction
    void renderSite() {
        File template = document.get()
        final String templateText = template.text
        File o = output.get()
        Map<String, String> m = siteMeta(title.get(),
                about.get(),
                url.get(),
                keywords.get() as List<String>,
                robots.get(),
                email.get(),
                authorName.get(),
                authorUrl.get(),
                authorAvatar.get()
        )
        List<Page> listOfPages = parsePages(pages.get())
        File tempFolder = new File(o.absolutePath + "/" + TEMP)
        if (!tempFolder.exists()) {
            tempFolder.mkdir()
        }
        listOfPages.addAll(parsePages(tempFolder))
        File dist = new File(o.absolutePath + "/" + DIST)
        renderPages(m, listOfPages, dist, templateText)
    }

    static Map<String, String> siteMeta(String title,
                                        String about,
                                        String url,
                                        List<String> keywords,
                                        String robots,
                                        String email,
                                        String authorName,
                                        String authorUrl,
                                        String authorAvatar) {
        [
                title: title,
                summary: about,
                url: url,
                keywords: keywords.join(','),
                robots: robots,
                email: email,
                author_name: authorName,
                author_url: authorUrl,
                author_avatar: authorAvatar

        ] as Map<String, String>
    }

    static void renderPages(Map<String, String> sitemeta, List<Page> listOfPages, File outputDir, final String templateText) {
        for (Page page : listOfPages) {
            Map<String, String> resolvedMetadata = processMetadata(sitemeta + page.metadata)
            String html = renderHtmlWithTemplateContent(page.content, resolvedMetadata, templateText)
            html = highlightMenu(html, sitemeta, page.path)
            if (page.body) {
                html = html.replace("<body>", "<body class='${page.body}'>")
            }
            saveHtmlToPath(outputDir, html, page.path)
        }
    }

    static void saveHtmlToPath(File outputDir, String html, String filepath) {
        File pageOutput = new File(outputDir.absolutePath)
        pageOutput.mkdir()
        String[] paths = filepath.split('/')
        for (String path : paths) {
            if (path.endsWith(".html")) {
                pageOutput = new File(pageOutput.getAbsolutePath() + "/" + path)
            } else if (path.trim().isEmpty()) {
                continue
            } else {
                pageOutput = new File(pageOutput.getAbsolutePath() + "/" + path)
                pageOutput.mkdir()
            }
        }
        pageOutput.createNewFile()
        pageOutput.text = html.replace('\n','')
    }

    static Map<String, String> processMetadata(Map<String, String> sitemeta) {
        Map<String, String> resolvedMetadata = sitemeta

        if (resolvedMetadata.containsKey("CSS")) {
            for (String css : (resolvedMetadata['CSS'].split(','))) {
                resolvedMetadata.put("CSS", "<link rel='stylesheet' href='" + css + "'/>")
            }
        } else {
            resolvedMetadata.put("CSS", "")
        }
        if (resolvedMetadata.containsKey("JAVASCRIPT")) {
            for (String js : (resolvedMetadata['JAVASCRIPT'].split(','))) {
                resolvedMetadata.put("JAVASCRIPT", "<script src='" + js + "'></script>")
            }
        } else {
            resolvedMetadata.put("JAVASCRIPT", "")
        }
        if (!resolvedMetadata.containsKey("HTML header")) {
            resolvedMetadata.put("HTML header", "")
        }
        if (!resolvedMetadata.containsKey("keywords")) {
            resolvedMetadata.put('keywords', "")
        }
        if (!resolvedMetadata.containsKey("summary")) {
            resolvedMetadata.put('summary', "")
        }
        if (!resolvedMetadata.containsKey("date_published")) {
            resolvedMetadata.put('date_published', BlogTask.JSON_FEED_FORMAT.format(new Date()))
        }
        if (!resolvedMetadata.containsKey("date_modified") && resolvedMetadata.containsKey("date_published")) {
            resolvedMetadata.put('date_modified', resolvedMetadata.get("date_published"))
        }
        if (!resolvedMetadata.containsKey("date_modified")) {
            resolvedMetadata.put('date_modified', BlogTask.JSON_FEED_FORMAT.format(new Date()))
        }
        if (!resolvedMetadata.containsKey(ROBOTS)) {
            resolvedMetadata.put('robots', "all")
        }
        if (!resolvedMetadata.containsKey("lang")) {
            resolvedMetadata.put('lang', "en")
        }
        String twittercard = ""
        if (resolvedMetadata.containsKey('video') || resolvedMetadata.containsKey('external_url')) {
            String videoId = parseVideoId(resolvedMetadata)
            if (videoId) {
                twittercard = metaTwitter('card', 'player') + twitterPlayerHtml(videoId, TWITTER_CARD_PLAYER_WIDTH, TWITTER_CARD_PLAYER_HEIGHT)
            }
        }
        if (resolvedMetadata.containsKey("banner_image")) {
            twittercard += metaTwitter('card','summary_large_image')
            twittercard += "<meta name=\"twitter:image\" content=\"${resolvedMetadata['banner_image']}\">"
        }
        twittercard += "<meta name=\"twitter:creator\" content=\"${TWITER_HANDLE}\">"
        twittercard += "<meta name=\"twitter:site\" content=\"${TWITER_HANDLE}\">"
        twittercard += metaTwitter('title',resolvedMetadata['title'])
        twittercard += metaTwitter('description',resolvedMetadata['summary'])
        resolvedMetadata['twittercard'] = twittercard

        resolvedMetadata['events'] = calendarHtml().orElse('');

        resolvedMetadata
    }

    static Optional<String> calendarHtml() {
        String html = null
        try {
            FileInputStream fin = new FileInputStream(ICAL_NAME);
            CalendarBuilder builder = new CalendarBuilder();
            net.fortuna.ical4j.model.Calendar calendar = builder.build(fin)
            List<VEvent> events = []
            for (CalendarComponent calendarComponent : calendar.getComponents()) {
                if (calendarComponent instanceof VEvent) {
                    VEvent e = (VEvent) calendarComponent;
                    events.add(e)
                }
            }
            for (VEvent e : events.stream().sorted(START_DATE_COMPARATOR).collect(Collectors.toList())) {
                Optional<String> htmlOptional = htmlOfEvent(e)
                if (htmlOptional.isPresent()) {
                    if (StringUtils.isEmpty(html)) {
                        html = "My next events:<br/>"
                    }
                    html += htmlOptional.get()
                }
            }
        } catch(IOException e) {

        } catch(ParserException e) {

        }
        StringUtils.isEmpty(html) ? Optional.empty() : Optional.of("<p>" + html + "</p>")
    }

    @CompileDynamic
    static Optional<String> htmlOfEvent(VEvent e) {
        Date startDate = e.getStartDate().getDate();
        if (startDate.after(new Date())) {
            return Optional.of("🗓 <span>"
            + new SimpleDateFormat("MMM dd HH:mm").format(startDate)
            + "</span> "
            + "<a href=\"" + e.getProperty("DESCRIPTION").getValue() + "\">" + e.getProperty("SUMMARY").getValue() + "</a>"
            + "</a><br/>")
        }
        Optional.empty()
    }

    @Nullable
    static String parseVideoId(Map<String, String> metadata) {
        String videoId = metadata.containsKey('external_url') &&
                metadata['external_url'].startsWith(YOUTUBE_WATCH) ?
                metadata['external_url'].substring(YOUTUBE_WATCH.length()) : null
        if (videoId) {
            return videoId
        }
        metadata.containsKey('video') &&
                metadata['video'].startsWith(YOUTUBE_WATCH) ?
                metadata['video'].substring(YOUTUBE_WATCH.length()) : null
    }

    @Nullable
    static String parseVideoIframe(Map<String, String> metadata) {
        String videoId = parseVideoId(metadata)
        videoId ? """\
<iframe width='100%' 
        height='360' 
        src='https://www.youtube-nocookie.com/embed/${videoId}' frameborder='0'></iframe>""" : null
    }

    static String twitterPlayerHtml(String videoId, int width, int height) {
"""\
<meta name='twitter:player' content='https://www.youtube.com/embed/${videoId}' />
<meta name='twitter:player:width' content='${width}' />
<meta name='twitter:player:height' content='${height}' />
"""
    }

    static String metaTwitter(String attribute, String cardType) {
        "<meta name='twitter:${attribute}' content='${cardType}'/>"
    }

    static String highlightMenu(String html, Map<String, String> sitemeta, String path) {
        html.replaceAll("<li><a href='" + sitemeta['url'] + path, "<li class='active'><a href='" + sitemeta['url'] + path)
    }

    static List<Page> parsePages(File pages) {
        List<Page> listOfPages = []
        pages.eachFileRecurse(FILES) { file ->
            if (file.path.endsWith(".html")) {
                ContentAndMetadata contentAndMetadata = parseFile(file)
                String filename = file.absolutePath.replace(pages.absolutePath, "")
                listOfPages << new Page(filename: filename, content: contentAndMetadata.content, metadata: contentAndMetadata.metadata)
            }
            if (file.path.endsWith(".md") || file.path.endsWith(".markdown")) {
                ContentAndMetadata contentAndMetadata = parseFile(file)
                String filename = file.absolutePath.replace(pages.absolutePath, "")
                listOfPages << new Page(filename: filename.replaceAll(".md", ".html").replaceAll(".markdown", ".html"), content: MarkdownUtil.htmlFromMarkdown(contentAndMetadata.content), metadata: contentAndMetadata.metadata)
            }
        }
        listOfPages
    }

    static ContentAndMetadata parseFile(File file) {
        String line = null
        List<String> lines = []
        Map<String, String> metadata = [:]
        boolean metadataProcessed = false
        int lineCount = 0
        file.withReader { reader ->
            while ((line = reader.readLine()) != null) {
                if (lineCount == 0 && line.startsWith(SEPARATOR)) {
                    continue
                }
                lineCount++
                if (!metadataProcessed && line.startsWith(SEPARATOR)) {
                    metadataProcessed = true
                    continue
                }
                if (!metadataProcessed && line.contains(COLON)) {
                    String metadataKey = line.substring(0, line.indexOf(COLON as String)).trim()
                    String metadataValue = line.substring(line.indexOf(COLON as String) + COLON.length()).trim()
                    metadata[metadataKey] = metadataValue
                }
                line = replaceLineWithMetadata(line, metadata)
                if (metadataProcessed) {
                    lines << line
                }
            }
        }

        !metadataProcessed || lines.isEmpty() ? new ContentAndMetadata(metadata: [:] as Map<String, String>, content: file.text) :
                new ContentAndMetadata(metadata: metadata, content: lines.join("\n"))
    }

    @Nonnull
    static String renderHtmlWithTemplateContent(@Nonnull @NotNull String html,
                                         @Nonnull @NotNull Map<String, String> meta,
                                         @NotNull @Nonnull final String templateText) {
        String outputHtml = templateText
        String result = outputHtml.replace(' data-document>', ">" + html)
        result = replaceLineWithMetadata(result, meta)
        result
    }

    static String formatDate(String date) {
        BlogTask.JSON_FEED_FORMAT.format(BlogTask.parseDate(date))
    }

    static String toGMTString(String date) {
        BlogTask.GMT_FORMAT.format(BlogTask.parseDate(date))
    }

    static String replaceLineWithMetadata(String line, Map<String, String> metadata) {
        Map<String, String> m = new HashMap<>(metadata)
        if (m.containsKey('date_published')) {
            m['date_published'] = toGMTString(m['date_published'])
        }
        if (m.containsKey('date_modified')) {
            m['date_modified'] = toGMTString(m['date_modified'])
        }
        for (String metadataKey : m.keySet()) {
            if (line.contains("[%${metadataKey}]".toString())) {
                String value = m[metadataKey]
                if ("[%${metadataKey}]".toString() == '[%author]') {
                    List<String> authors = value.split(",") as List<String>
                    value = '<span class="author">By ' + authors.join("<br/>") + '</span>'
                    line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                } else if ("[%${metadataKey}]".toString() == '[%author.name]') {
                    value = '<span class="author">' + value + '</span>'
                    line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                } else if ("[%${metadataKey}]".toString() == '[%date_published]') {
                    if (line.contains('<meta')) {
                        line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                    } else {
                        value = '<span class="date">' + value + '</span>'
                        line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                    }
                } else {
                    line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
                }
            }
        }
        if (line.endsWith('mp4') && line.startsWith('https')) {
            return "<video controls src='${line}'/>"
        }
        line
    }
}
