package com.groovycalamari.iawriterblog.gradle

import com.groovycalamari.iawriterblog.Post
import com.groovycalamari.iawriterblog.PostProcessor
import com.groovycalamari.iawriterblog.PostRenderer
import com.groovycalamari.iawriterblog.PostRssRenderer
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CompileStatic
class RenderRss extends DefaultTask {

    @Input
    final Property<String> title = project.objects.property(String)

    @Input
    final Property<String> about = project.objects.property(String)

    @Input
    final Property<String> url = project.objects.property(String)


    File sourceDir

    File outputDir

    File rss

    @OutputDirectory
    File getOutputDir() {
        return this.outputDir
    }

    @OutputFile
    File getRss() {
        return this.rss
    }

    @InputDirectory
    File getSourceDir() {
        return this.sourceDir
    }

    RenderRss() {
        File archive = project.file('archive')
        if (archive.exists()) {
            sourceDir = archive
        }
        outputDir = project.buildDir
        rss = new File("${project.buildDir}/rss.xml".toString())
    }

    @TaskAction
    void renderBlog() {
        PostProcessor postProcessor = new PostProcessor()
        List<Post> posts = postProcessor.run(sourceDir)
        PostRenderer postRenderer = new PostRenderer()
        PostRssRenderer postRssRenderer = new PostRssRenderer()
        postRssRenderer.render(posts, title.get(), url.get(), about.get(), rss, postRenderer)

    }
}
