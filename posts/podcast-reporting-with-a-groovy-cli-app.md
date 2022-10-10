---
title: Groovy CLI application for Podcast reporting
summary: I wrote a small Micronaut command line application to output a CSV file with a podcast's episode titles, authors, and release dates.
date_published: 2022-10-10T18:11:44+01:00
keywords:micronaut,groovy,picocli
external_url: https://github.com/micronaut-advocacy/cli-podcastreport
---

# [%title]

I wrote a small [Micronaut Framework](https://micronaut.io) command line [application to output a CSV file with a podcast's episode titles, authors, and release dates](https://github.com/micronaut-advocacy/cli-podcastreport). Micronaut CLI applications use [Picocli](https://picocli.info).

Working with XML and [Apache Groovy](https://groovy-lang.org) is easy.

The core functionality is  only a few lines of Apache Groovy code: 

```groovy
@CompileDynamic
void run() {
    String text = new URL(url).text
    def rss = new XmlSlurper().parseText(text)
    List<Episode> episodeList = []
    String publication = DateTimeFormatter.ISO_DATE.format(
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
            .parse(it.pubDate as String))
    rss.channel.item.each {
        episodeList << new Episode(title: it.title, 
                                   author: it.author, 
                                   publication: publication)
    String result = episodeList.sort({a, b -> b.publication <=> a.publication})
        .collect {it -> "${it.title},${it.author},${it.publication}"}.join("\n")
    if (verbose) {
        println result
    }
    output.text = result
}
```


