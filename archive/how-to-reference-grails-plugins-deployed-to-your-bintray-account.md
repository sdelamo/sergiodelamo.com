title: How to reference  Grails Plugins deployed to your bintray account
date: Aug 6, 2017
---

# [%title]

[%date] #grails

Add a new Maven Url entry to your dependencies block:

```groovy
repositories {
    mavenLocal()
    maven { url "https://repo.grails.org/grails/core" }
    maven { url 'http://dl.bintray.com/sdelamo/plugins' }
}
```

