package com.groovycalamari.iawriterblog.gradle

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin

@CompileStatic
class IAWriterBlogPlugin implements Plugin<Project> {

    public static final String TASK_RENDER_TAGS = 'renderTags'
    public static final String TASK_RENDER_ARCHIVE = 'renderArchive'
    public static final String TASK_RENDER_BLOG = 'renderBlog'
    public static final String EXTENSION_SITE = 'site'
    public static final String TASK_COPY_TEMPLATE_CSS = 'copyTemplateCss'
    public static final String TASK_COPY_TEMPLATE_FONTS = 'copyTemplateFonts'
    public static final String TASK_COPY_TEMPLATE_IMAGES = 'copyTemplateImages'
    public static final String TASK_COPY_SOURCE_IMAGES = 'copySourceImages'
    public static final String TASK_COPY_TEMPLATE_JAVASCRIPT = 'copyTemplateJavascript'
    public static final String TASK_RENDER_RSS = 'renderRss'
    public static final String TASK_BUILD = "build"
    public static final String IATEMPLATE = "iatemplate"
    public static final String[] IMAGE_EXTENSIONS = ["*.png", "*.svg", "*.jpg", "*.jpeg"] as String[]
    public static final String SOURCE_DIR = "sourceDir"
    public static final String OUTPUT_DIR = "outputDir"

    void apply(Project project) {
        project.getPlugins().apply(BasePlugin.class)
        project.extensions.create(EXTENSION_SITE, SiteExtension)
        project.tasks.register(TASK_RENDER_BLOG, RenderBlog, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("title", siteExtension.title)
                task.setProperty("url", siteExtension.url)
                task.setProperty("about", siteExtension.about)
                if (siteExtension.sourceDir.isPresent()) {
                    task.setProperty(SOURCE_DIR, siteExtension.sourceDir.get())
                }
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }
                if (siteExtension.robots.isPresent()) {
                    task.setProperty("robots", siteExtension.robots.get())
                }
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty(IATEMPLATE, siteExtension.iatemplate)
            }
        })

        project.tasks.register(TASK_RENDER_ARCHIVE, RenderArchive, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("title", siteExtension.title)
                task.setProperty("url", siteExtension.url)
                task.setProperty("about", siteExtension.about)
                if (siteExtension.sourceDir.isPresent()) {
                    task.setProperty(SOURCE_DIR, siteExtension.sourceDir.get())
                }
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }
                if (siteExtension.robotsArchive.isPresent()) {
                    task.setProperty("robots", siteExtension.robotsArchive.get())
                }
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty(IATEMPLATE, siteExtension.iatemplate)
            }
        })
        project.tasks.register(TASK_RENDER_TAGS, RenderTags, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("title", siteExtension.title)
                task.setProperty("url", siteExtension.url)
                task.setProperty("about", siteExtension.about)
                if (siteExtension.sourceDir.isPresent()) {
                    task.setProperty(SOURCE_DIR, siteExtension.sourceDir.get())
                }
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }
                if (siteExtension.robotsTags.isPresent()) {
                    task.setProperty("robots", siteExtension.robotsTags.get())
                }
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty(IATEMPLATE, siteExtension.iatemplate)
            }
        })

        project.tasks.register(TASK_COPY_TEMPLATE_CSS, CopyTemplateCss, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty(IATEMPLATE, siteExtension.iatemplate)
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }

            }
        })
        project.tasks.register(TASK_COPY_TEMPLATE_FONTS, CopyTemplateFonts, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty(IATEMPLATE, siteExtension.iatemplate)
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }

            }
        })
        project.tasks.register(TASK_COPY_TEMPLATE_IMAGES, CopyTemplateImages, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty(IATEMPLATE, siteExtension.iatemplate)
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }
            }
        })
        project.tasks.register(TASK_COPY_TEMPLATE_JAVASCRIPT, CopyTemplateJavascript, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty(IATEMPLATE, siteExtension.iatemplate)
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }
            }
        })
        project.tasks.register(TASK_RENDER_RSS, RenderRss, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("title", siteExtension.title)
                task.setProperty("url", siteExtension.url)
                task.setProperty("about", siteExtension.about)
                if (siteExtension.sourceDir.isPresent()) {
                    task.setProperty(SOURCE_DIR, siteExtension.sourceDir.get())
                }
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }
            }
        })

        project.tasks.register(TASK_COPY_SOURCE_IMAGES, CopySourceImages, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_SITE)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                if (siteExtension.sourceDir.isPresent()) {
                    task.setProperty(SOURCE_DIR, siteExtension.sourceDir.get())
                }
                if (siteExtension.outputDir.isPresent()) {
                    task.setProperty(OUTPUT_DIR, siteExtension.outputDir.get())
                }
            }
        })

        project.tasks.named(TASK_BUILD).configure(new Action<Task>() {
            @Override
            void execute(Task task) {
                task.dependsOn(TASK_RENDER_BLOG)
                task.dependsOn(TASK_RENDER_ARCHIVE)
                task.dependsOn(TASK_RENDER_TAGS)
                task.dependsOn(TASK_COPY_TEMPLATE_CSS)
                task.dependsOn(TASK_COPY_TEMPLATE_FONTS)
                task.dependsOn(TASK_COPY_TEMPLATE_IMAGES)
                task.dependsOn(TASK_COPY_TEMPLATE_JAVASCRIPT)
                task.dependsOn(TASK_COPY_TEMPLATE_CSS)
                task.dependsOn(TASK_RENDER_RSS)
                task.dependsOn(TASK_COPY_SOURCE_IMAGES)
            }
        })
    }
}
