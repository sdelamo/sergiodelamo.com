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
    String authors
    String publicationDate
    
    String getQuarter() {
        String month = publicationDate.substring(5,7)
        if (month == '01' || month == '02' || month == '03') {
            return 'Q1'
        } else if (month == '04' || month == '05' || month == '06') {
           return 'Q2'
        } else if (month == '07' || month == '08' || month == '09') {
           return 'Q3'
        } else {
            return 'Q4'
        }
    }
}
def result = new JsonSlurper().parseText(new URL("https://guides.micronaut.io/latest/guides.json").text)
List<Guide> guides = result.collect { new Guide(title: it.title, publicationDate: it.publicationDate, authors: it.authors.join('/')) }

guides = guides.sort({ a, b -> 
    a.publicationDate <=> b.publicationDate 
}).reverse()
File f = new File("/Users/sdelamo/Downloads/guides.csv")
String text = ""
for (Guide guide : guides) {
text += guide.quarter + "," + guide.title + ",Guide,"+ guide.authors +","+ guide.publicationDate +"\n"
}
f.text = text
```