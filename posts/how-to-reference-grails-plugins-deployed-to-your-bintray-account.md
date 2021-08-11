---
title: How to reference  Grails Plugins deployed to your bintray account
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2017-08-06T07:04:00+01:00
date_modified: 2017-08-06T07:04:00+01:00
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