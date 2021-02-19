---
title: Geb Programmer : Obtain current page html source?
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-10-02T17:54:00+01:00
date_modified: 2016-10-02T17:54:00+01:00
---

# [%title]

[%author.name] [%date_published]

Tags: #geb #grails

Geb manual:

> Geb builds on the WebDriver browser automation library, which means that Geb can work with any browser that WebDriver can.

With WebDriver is easy to obtain the page source. Lets do a test to verify it.

Lets start by creating a Gradle project with Groovy

```bash
$ gradle --version

------------------------------------------------------------
Gradle 3.1
------------------------------------------------------------
$ mkdir pagesource
$ cd page source
$ touch &lt;i&gt;build.gradle&lt;/i&gt;
```

Add Groovy dependency to build.gradle

```groovy
apply plugin: 'groovy'
```

Add Gradle wrapper

```bash
$ gradle wrapper --gradle-version 3.1
```

add Geb and Spock depependencies to build.gradle

```groovy
apply plugin: 'groovy'

repositories {
    jcenter()
}
dependencies {
    compile "org.gebish:geb-core:1.0-rc-1"
    compile "org.seleniumhq.selenium:selenium-firefox-driver:2.52.0"
    compile "org.seleniumhq.selenium:selenium-support:2.52.0"
    compile 'org.spockframework:spock-core:1.0-groovy-2.4'
}
```
Create a test

```bash
$ vi &lt;i&gt;src/test/groovy/PageSourceSpec.groovy&lt;/i&gt;
```

```groovy
import spock.lang.Specification

import geb.Browser

class PageSourceSpec extends Specification {
   def "test page source of sergiodeamo.es has a closing &lt;/body&gt; tag"() {
	when:
        def browser = new Browser()
        browser.go 'http://sergiodelamo.es'

        then:
        browser.driver.pageSource.contains('&lt;/body&gt;')
   }
}
```

If you run the test, it will pass:

```bash
$ ./gradlew test
```
