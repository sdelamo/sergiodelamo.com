---
title: How to reference  Grails Plugins deployed to your bintray account
date_published: 2017-08-06T07:04:00+01:00
keywords:grails,gradle
---

# [%title]

Add a new Maven Url entry to your dependencies block:

```groovy
repositories {
    mavenLocal()
    maven { url "https://repo.grails.org/grails/core" }
    maven { url 'http://dl.bintray.com/sdelamo/plugins' }
}
```