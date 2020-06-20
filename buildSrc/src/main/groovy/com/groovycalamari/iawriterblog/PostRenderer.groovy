package com.groovycalamari.iawriterblog

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.MutableDataSet
import edu.umd.cs.findbugs.annotations.NonNull
import groovy.transform.CompileStatic

import javax.annotation.Nonnull
import javax.validation.Valid
import javax.validation.constraints.NotNull

@CompileStatic
class PostRenderer implements com.groovycalamari.iawriterblog.HtmlRenderer {

    MutableDataSet options = new MutableDataSet()
    Parser parser = Parser.builder(options).build()
    HtmlRenderer renderer = HtmlRenderer.builder(options).build()

    void render(@Nonnull @NotNull Map<String, String> sitemeta,
                @NonNull @NotNull List<Post> posts,
                File template,
                File outputDirectory) {
        String templateText = template.text
        for (Post post : posts) {
            File output = new File(outputDirectory.getAbsolutePath() + "/" + post.path)
            output.createNewFile()
            output.text = renderPostWithTemplateContent(sitemeta, post, templateText)
        }
    }

    @Nonnull
    String wrapTags(@Nonnull @NotNull String html) {
        html.split("\n")
                .collect { line ->
                    if (line.startsWith("<p>") && line.endsWith("</p>")) {
                        String lineWithoutParagraphs = line.replaceAll("<p>", "")
                                .replaceAll("</p>", "")
                        String[] words = lineWithoutParagraphs.split(" ")
                        lineWithoutParagraphs = words.collect { word ->
                            if (word.startsWith("#")) {
                                String tag = word
                                if (word.contains("<")) {
                                    tag = word.substring(0, word.indexOf("<"))
                                }
                                return "<a href=\"/${tag.replaceAll("#", "")}.html\"><span class=\"hashtag\">${tag}</span></a>".toString()
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

    @Nonnull
    private String renderPostWithTemplateContent(@Nonnull @NotNull Map<String, String> sitemeta,
                                                 @Nonnull @NotNull @Valid Post post,
                                                 @NotNull @Nonnull String templateText) {
        String html = post instanceof MarkdownPost ? render(post) : post.content
        html = wrapTags(html)
        renderHtmlWithTemplateContent(html, sitemeta + post.metadata, templateText)
    }

    @Nonnull
    String renderHtmlWithTemplateContent(@Nonnull @NotNull String html,
                                         @Nonnull @NotNull Map<String, String> meta,
                                         @NotNull @Nonnull String templateText) {
        String outputHtml = templateText
        String result = outputHtml.replaceAll(' data-document>', ">" + html)
        result = PostProcessor.replaceLineWithMetadata(result, meta)
        result
    }

    @Nonnull
   String render(@Nonnull @NotNull @Valid MarkdownPost post) {
        Node document = parser.parse(post.content)
        return renderer.render(document)
    }
}
