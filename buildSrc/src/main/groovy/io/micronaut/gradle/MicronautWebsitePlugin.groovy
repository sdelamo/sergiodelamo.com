package io.micronaut.gradle

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin

@CompileStatic
class MicronautWebsitePlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "micronaut"
    public static final String TASK_BUILD = "build"
    public static final String TASK_GEN_SITE = "renderSite"
    public static final String TASK_GEN_SITEMAP = "genSitemap"
    public static final String CLEAN = "clean"
    public static final String TASK_COPY_ASSETS = "copyAssets"
    public static final String GROUP_MICRONAUT = 'micronaut'
    public static final String TASK_RENDER_BLOG = 'renderBlog'

    @Override
    void apply(Project project) {
        project.getPlugins().apply(BasePlugin.class)
        project.extensions.create(EXTENSION_NAME, SiteExtension)

        project.tasks.register(TASK_GEN_SITEMAP, SitemapTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("output", siteExtension.output)
                task.setProperty("url", siteExtension.url)
            }
            task.setGroup(GROUP_MICRONAUT)
            task.setDescription('Generates build/dist/sitemap.xml with every page in the site')
        })
        project.tasks.register(TASK_RENDER_BLOG, BlogTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("url", siteExtension.url)
                task.setProperty("title", siteExtension.title)
                task.setProperty("about", siteExtension.description)
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty("robots", siteExtension.robots)
                task.setProperty("document", siteExtension.template)
                task.setProperty("output", siteExtension.output)
                task.setProperty("posts", siteExtension.posts)
                task.setProperty("assets", siteExtension.assets)
            }
            task.setGroup(GROUP_MICRONAUT)
            task.description = 'Renders Markdown posts (posts/*.md) into HTML pages (dist/blog/*.html). It generates tag pages. Generates RSS feed. Posts with future dates are not generated.'
        })
        project.tasks.register(TASK_COPY_ASSETS, CopyAssetsTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("output", siteExtension.output)
                task.setProperty("assets", siteExtension.assets)
            }
            task.setDescription('Copies css, js, fonts and images from the assets folder to the dist folder')
            task.setGroup(GROUP_MICRONAUT)
        })
        project.tasks.register(TASK_GEN_SITE, RenderSiteTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_NAME)
            if (extension instanceof SiteExtension) {
                SiteExtension siteExtension = ((SiteExtension) extension)
                task.setProperty("url", siteExtension.url)
                task.setProperty("title", siteExtension.title)
                task.setProperty("about", siteExtension.description)
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty("robots", siteExtension.robots)
                task.setProperty("document", siteExtension.template)
                task.setProperty("output", siteExtension.output)
                task.setProperty("pages", siteExtension.pages)
            }
            task.setGroup(GROUP_MICRONAUT)
            task.dependsOn(TASK_COPY_ASSETS)
            task.finalizedBy(TASK_RENDER_BLOG)
            task.finalizedBy(TASK_GEN_SITEMAP)
            task.setDescription('Build Micronaut website - generates pages with HTML entries in pages and build/temp, renders blog and RSS feed, copies assets and generates a sitemap')

        })

        project.tasks.named(TASK_BUILD).configure(new Action<Task>() {
            @Override
            void execute(Task task) {
                task.dependsOn(TASK_GEN_SITE)
            }
        })

    }
}
