package com.groovycalamari.iawriterblog.gradle

import com.groovycalamari.iawriterblog.Post
import com.groovycalamari.iawriterblog.PostProcessor
import com.groovycalamari.iawriterblog.PostRenderer
import groovy.transform.CompileStatic
import groovy.transform.Internal
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class RenderArchive extends DefaultTask {

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

    @OutputDirectory
    File getOutputDir() {
        return this.outputDir
    }

    @InputDirectory
    File getSourceDir() {
        return this.sourceDir
    }

    RenderArchive() {
        File archive = project.file('archive')
        if (archive.exists()) {
            sourceDir = archive
        }
        outputDir = project.buildDir
        robots = ROBOTS_NOINDEX
    }

    @TaskAction
    void renderArchive() {
        PostProcessor postProcessor = new PostProcessor()
        List<Post> posts = postProcessor.run(sourceDir)
        PostRenderer postRenderer = new PostRenderer()
        String html = "<h1>Blog</h1>" + posts.findAll { it.isBlogPost() }
                .collect { post -> "<h2><a href=\"/${post.path}\">${post.title}</a></h2><p>${post.description ?: ''}</p>" }.join("\n")
        String templateText = template.get().text
        String renderedHtml = postRenderer.renderHtmlWithTemplateContent(html, sitemeta(), templateText)
        File output = new File(outputDir.getAbsolutePath() + "/archive.html")
        output.createNewFile()
        output.text = renderedHtml
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
