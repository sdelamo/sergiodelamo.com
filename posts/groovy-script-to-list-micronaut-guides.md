---
title: Apache Groovy script to list Micronaut Guides
summary: Apache Groovy is extremely powerful to write a script quickly
date_published: 2022-09-30T18:40:17+01:00
keywords:groovy
---

# [%title]

[%summary]


```groovy
import groovy.json.JsonSlurper
class Guide {
    String title
    String publicationDate
}

def result = new JsonSlurper().parseText(new URL("https://guides.micronaut.io/latest/guides.json").text)
List<Guide> guides = result.collect { new Guide(title: it.title, publicationDate: it.publicationDate) }

guides = guides.sort({ a, b -> 
    a.publicationDate <=> b.publicationDate 
}).reverse()
File f = new File("/Users/sdelamo/Downloads/guides.txt")
String text = ""
for (Guide guide : guides) {
text += guide.publicationDate + " " + guide.title + "\n"
}
f.text = text
```