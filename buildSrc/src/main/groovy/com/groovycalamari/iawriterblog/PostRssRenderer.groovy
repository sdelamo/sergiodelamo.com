package com.groovycalamari.iawriterblog

import io.micronaut.http.uri.UriBuilder
import io.micronaut.rss.DefaultRssFeedRenderer
import io.micronaut.rss.RssChannel
import io.micronaut.rss.RssFeedRenderer
import io.micronaut.rss.RssItem

class PostRssRenderer {

    void render(List<Post> posts,
                String title,
                String url,
                String description,
                File output,
                HtmlRenderer htmlRenderer) {
        RssChannel.Builder builder = RssChannel.builder(title, url, description)
        //builder.pubDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
        //builder.lastBuildDate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("GMT")))
//                .docs("http://blogs.law.harvard.edu/tech/rss")
//                .generator("Weblog Editor 2.0")
//                .managingEditor("editor@example.com")
//                .webMaster("webmaster@example.com")

        for (MarkdownPost post : posts) {
            builder.item(RssItem.builder()
                    .title(post.getTitle())
                    .link(UriBuilder.of(URI.create(url)).path(post.path).toString())
                    .guid(post.path)
                    .description(htmlRenderer.render(post))
                    .build())
        }
        FileWriter writer = new FileWriter(output)
        RssFeedRenderer rssFeedRenderer = new DefaultRssFeedRenderer()
        rssFeedRenderer.render(writer, builder.build())
        writer.close()
    }

    Map<String, String> getRssAttributes() {
        Map<String, String> m = new HashMap<>();
        m.put("version", "2.0");
        m.put("xmlns:content", "http://purl.org/rss/1.0/modules/content/");
        return m;
    }
}
