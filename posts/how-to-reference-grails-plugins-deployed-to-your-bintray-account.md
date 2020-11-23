---
title: How to reference  Grails Plugins deployed to your bintray account
date: Aug 06, 2017 07:04
---

# [%title]

[%date]

Tags: #grails #gradle

Add a new Maven Url entry to your dependencies block:

```groovy
repositories {
    mavenLocal()
    maven { url "https://repo.grails.org/grails/core" }
    maven { url 'http://dl.bintray.com/sdelamo/plugins' }
}
```