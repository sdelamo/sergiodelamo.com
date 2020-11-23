---
title:  Gradle Podcast plugin
date_published: 2020-09-21T06:07:00+01:00
date_modified: 2020-09-21T06:07:00+01:00
summary: Gradle podcast plugin allows you to create a Podcast RSS feed with a  Gradle plugin extension and a set of markdown files for your show episodes. 
---

# [%title]

[%date]

Tags: #gradle #rss

Last week, I published my first [Gradle plugin](https://plugins.gradle.org). It eases the creation of a Podcast RSS feed. Internally it uses [micronaut-itunespodcast](https://micronaut-projects.github.io/micronaut-rss/latest/guide/index.html#itunespodcast) module.

The plugin has an extension to configure your Podcast metadata

```groovy
podcast {
    episodes = file("episodes")
    title = 'Hiking Treks'
    link = 'https://www.apple.com/itunes/podcasts/'
    language = 'en-us'
    copyright = '&#169; 2020 John Appleseed'
    description = 'Love to get outdoors and discover nature&apos;s treasures? Hiking Treks is the show for you. We review hikes and excursions, review outdoor gear and interview a variety of naturalists and adventurers. Look for new episodes each week.'
    author = "The Sunset Explorers"
    type = 'SERIAL'
    owner {
        name = "Sunset Explorers"
        email = "mountainscape@icloud.com"
    }
    image {
        url = "https://applehosted.podcasts.apple.com/hiking_treks/artwork.png"
    }
    block = false
    categories = 'SPORTS_WILDERNESS'
    explicit = false
}
```

You write your episode show notes in Markdown and use metadata to configure the audio files associated with the episode:

_episodes/aae20190425.md_
```md
---
episodeType: TRAILER
episode: 1
season: 1
title: Hiking Treks Trailer
enclosureLength: 498537
enclosureType: audio/mpeg
enclosureUrl: http://example.com/podcasts/everything/AllAboutEverythingEpisode4.mp3
guid: aae20190418
pubDate: 2019-01-08T01:15:00Z[GMT]
duration: 1079
explicit: false
---

The Sunset Explorers share tips, techniques and recommendations for great hikes and adventures around the United States. Listen on [Apple Podcasts](https://www.apple.com/itunes/podcasts/)
```

Run `./gradlew build` and it generates the following RSS feed: 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<rss xmlns:content="http://purl.org/rss/1.0/modules/content/" 
     version="2.0" xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd">
    <channel>
        <title>Hiking Treks</title>
        <link>https://www.apple.com/itunes/podcasts/</link>
        <image>
            <title>Hiking Treks</title>
            <link>https://www.apple.com/itunes/podcasts/</link>
            <url>https://applehosted.podcasts.apple.com/hiking_treks/artwork.png</url>
        </image>
        <description>Love to get outdoors and discover nature&amp;apos;s treasures? Hiking Treks is the show for you. We review hikes and excursions, review outdoor gear and interview a variety of naturalists and adventurers. Look for new episodes each week.</description>
        <language>en-us</language>
        <copyright>&amp;#169; 2020 John Appleseed</copyright>
        <category text="Sports">
            <category text="Wilderness">
            </category>
        </category>
        <itunes:author>The Sunset Explorers</itunes:author>
        <itunes:type>serial</itunes:type>
        <itunes:owner>
            <itunes:name>Sunset Explorers</itunes:name>
            <itunes:email>mountainscape@icloud.com</itunes:email>
        </itunes:owner>
        <itunes:image href="https://applehosted.podcasts.apple.com/hiking_treks/artwork.png"></itunes:image>
        <itunes:category text="Sports">
            <itunes:category text="Wilderness"></itunes:category>
        </itunes:category>
        <itunes:explicit>no</itunes:explicit>
        <itunes:block>no</itunes:block>
    <item>
        <title>Hiking Treks Trailer</title>
        <enclosure length="498537"
                   type="audio/mpeg"
                   url="http://example.com/podcasts/everything/AllAboutEverythingEpisode4.mp3"></enclosure>
        <guid>aae20190418</guid>
        <itunes:episodeType>trailer</itunes:episodeType>
        <itunes:title>Hiking Treks Trailer</itunes:title>
        <content:encoded>&lt;p&gt;The Sunset Explorers share tips, techniques and recommendations for
great hikes and adventures around the United States. Listen on &lt;a href="https://www.apple.com/itunes/podcasts/"&gt;Apple Podcasts&lt;/a&gt;&lt;/p&gt;
        </content:encoded>
        <itunes:duration>1079</itunes:duration>
        <itunes:episode>1</itunes:episode>
        <itunes:season>1</itunes:season>
        <itunes:explicit>no</itunes:explicit>
    </item>
    </channel>
</rss>
```

My goal is to enable podcast creators to generate a RSS feed easily. It should make easy to statically host a Podcast RSS feed. It should help keep Podcasts open.

[Podcast Gradle Plugin documentation](https://sdelamo.github.io/podcast-gradle-plugin/index.html)