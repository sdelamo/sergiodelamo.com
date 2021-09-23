package io.micronaut.gradle

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

import static groovy.io.FileType.FILES

@CompileStatic
class SitemapTask extends DefaultTask {
    static final String FILE_SITEMAP = 'sitemap.xml'
    public static final String LAST_MODIFIED = 'last-modified" content="'
    public static final String DOT_HTML = '.html'

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @Input
    final Property<String> url = project.objects.property(String)

    @TaskAction
    void renderSitemap() {
        String websiteUrl = url.get()
        File folder = inputFolder()
        Map<String, File> urlsToFiles = [:]
        folder.eachFileRecurse(FILES) {
            if (it.name.endsWith(DOT_HTML)) {
                urlsToFiles.put("${websiteUrl}${it.absolutePath.replace(folder.absolutePath, "")}".toString(), it)
            }
        }
        File outputFile = Paths.get(folder.absolutePath, FILE_SITEMAP).toFile()
        outputFile.createNewFile()
        outputFile.text = sitemapContent(urlsToFiles)
    }

    private File inputFolder() {
        new File(output.get().absolutePath, RenderSiteTask.DIST)
    }

    @CompileDynamic
    static String sitemapContent(Map<String, File> urlsToFiles) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.urlset(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9") {
            for (String urlStr : urlsToFiles.keySet()) {
                url {
                    loc urlStr
                    File f = urlsToFiles.get(urlStr)
                    if (f.text.indexOf(LAST_MODIFIED) != -1) {
                        String t = f.text.substring(f.text.indexOf(LAST_MODIFIED) + LAST_MODIFIED.length())
                        t = t.substring(0, t.indexOf('"'))
                        lastmod BlogTask.JSON_FEED_FORMAT.format(BlogTask.GMT_FORMAT.parse(t))
                    }
                }
            }
        }
        '<?xml version="1.0" encoding="UTF-8"?>\n' + writer.toString()
    }
}