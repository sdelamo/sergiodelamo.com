package com.groovycalamari.iawriterblog.gradle

import com.groovycalamari.iawriterblog.MarkdownPost
import com.groovycalamari.iawriterblog.Post
import com.groovycalamari.iawriterblog.PostProcessor
import com.groovycalamari.iawriterblog.PostRenderer
import com.groovycalamari.iawriterblog.PostRssRenderer
import com.groovycalamari.iawriterblog.Type
import groovy.transform.CompileStatic
import groovy.transform.Internal
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CompileStatic
class RenderBlog extends DefaultTask {

    public static final String ROBOTS_ALL = "all"
    final static String HASHTAG_SPAN = "<span class=\"hashtag\">#"
    final static String SPAN_CLOSE = "</span>"

    public static final String ROBOTS_NOINDEX = "noindex"

    @Input
    final Property<File> iatemplate = project.objects.property(File)

    @Input
    final Property<String> title = project.objects.property(String)

    @Input
    final Property<String> about = project.objects.property(String)

    @Input
    final Property<String> url = project.objects.property(String)

    @Input
    final ListProperty<String> keywords = project.objects.listProperty(String)

    @Input
    final String robots

    @Internal
    @Input
    final Provider<File> template = iatemplate.map { new File(it.absolutePath + '/Contents/Resources/document.html') }

    File sourceDir

    File outputDir

    File rss

    @OutputDirectory
    File getOutputDir() {
        return this.outputDir
    }

    @InputDirectory
    File getSourceDir() {
        return this.sourceDir
    }

    @OutputFile
    File getRss() {
        return this.rss
    }


    RenderBlog() {
        File archive = project.file('archive')
        if (archive.exists()) {
            sourceDir = archive
        }
        outputDir = project.buildDir
        robots = ROBOTS_ALL
        rss = new File("${project.buildDir}/rss.xml".toString())
    }

    @TaskAction
    void renderBlog() {
        PostProcessor postProcessor = new PostProcessor()
        List<Post> entries = postProcessor.run(sourceDir)
        PostRenderer postRenderer = new PostRenderer()
        postRenderer.render(sitemeta(), entries, template.get(), outputDir)

        List<Post> posts = entries.findAll { it.type == Type.POST }
        renderArchive(posts, postRenderer)
        renderTags(posts, postRenderer)
        renderRss(posts)
    }

    void renderArchive(List<Post> posts, PostRenderer postRenderer) {
        String html = "<h1>Blog</h1>" + posts.collect { post ->
            "<h2><a href=\"/${post.path}\">${post.title}</a></h2><p>${post.description ?: ''}</p>"
        }.join("\n")
        String templateText = template.get().text
        String renderedHtml = postRenderer.renderHtmlWithTemplateContent(html, sitemeta(), templateText)
        File output = new File(outputDir.getAbsolutePath() + "/archive.html")
        output.createNewFile()
        output.text = renderedHtml
    }

    void renderRss(List<Post> posts) {
        PostRenderer postRenderer = new PostRenderer()
        PostRssRenderer postRssRenderer = new PostRssRenderer()
        postRssRenderer.render(posts, title.get(), url.get(), about.get(), rss, postRenderer)
    }

    void renderTags(List<Post> posts, PostRenderer postRenderer) {
        Set<String> tags = []
        Map<String, List<String>> tagPosts = [:]
        for(Post post : posts) {
            String html = post instanceof MarkdownPost ? postRenderer.render(post) : post.content
            html = postRenderer.wrapTags(html)
            for (;;) {
                if (!(html.contains(HASHTAG_SPAN) && html.contains(SPAN_CLOSE))) {
                    break
                }
                html = html.substring(html.indexOf(HASHTAG_SPAN) + HASHTAG_SPAN.length())
                String tag = html.substring(0, html.indexOf(SPAN_CLOSE))
                tags << tag
                if (!tagPosts.containsKey(tag)) {
                    tagPosts[tag] = []
                }
                tagPosts[tag] << post.path
                html = html.substring(html.indexOf(SPAN_CLOSE) + SPAN_CLOSE.length())
            }
        }
        String templateText = template.get().text

        String html = "<h1>Tags</h1>" + tags.collect { tag -> "<h2><a href=\"/${tag}.html\">${tag}</a></h2>" }.join("\n")
        String renderedHtml = postRenderer.renderHtmlWithTemplateContent(html, sitemeta() + [title: "Tags"], templateText)
        File output = new File(outputDir.getAbsolutePath() + "/tags.html")
        output.createNewFile()
        output.text = renderedHtml

        for (String tag : tags) {
            html = "<h1>${tag}</h1>" + posts.findAll {
                tagPosts[tag].contains(it.path) && it.getType()
            }.collect { post -> "<h2><a href=\"/${post.path}\">${post.title}</a></h2><p>${post.description ?: ''}</p>" }.join("\n")

            renderedHtml = postRenderer.renderHtmlWithTemplateContent(html, sitemeta() + [title: tag], templateText)
            output = new File(outputDir.getAbsolutePath() + "/${tag}.html")
            output.createNewFile()
            output.text = renderedHtml
        }
    }

    Map<String, String> sitemeta() {
        [
                title: title.get(),
                url: url.get(),
                about: about.get(),
                robots: robots,
                keywords: keywords.get().join(','),
        ] as Map<String, String>
    }
}
