---
title: Geb Programmer : How to execute Geb with different browsers
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-10-10T06:33:00+01:00
date_modified: 2016-10-10T06:33:00+01:00
---

# [%title]

[%author.name] [%date_published]

Tags: #geb

```bash
$ gradle --version

------------------------------------------------------------
Gradle 3.1
------------------------------------------------------------
$ mkdir gwebbot_greach
$ cd page gwebbot_greach
$ touch &amp;lt;i&amp;gt;build.gradle&amp;lt;/i&amp;gt;
```

Add Groovy dependency to build.gradle

```groovy
apply plugin: 'groovy'
```


Add Gradle wrapper

```bash
$ gradle wrapper --gradle-version 3.1
```

add Geb, Selenium, Drivers and Spock depependencies to build.gradle

```groovy
apply plugin: 'groovy'

repositories {
    jcenter()
}
ext {
    seleniumVersion = '2.52.0'
    phantomJsDriverVersion = '1.2.1'
}
dependencies {
    compile "org.gebish:geb-core:1.0"
    compile "org.seleniumhq.selenium:selenium-support:${seleniumVersion}"
    compile "org.seleniumhq.selenium:selenium-firefox-driver:${seleniumVersion}"
    compile "org.seleniumhq.selenium:selenium-chrome-driver:${seleniumVersion}"
    compile("com.codeborne:phantomjsdriver:$phantomJsDriverVersion")
    compile 'org.spockframework:spock-core:1.0-groovy-2.4'
}

```

Create a test src/test/groovy/gebwebbot/greach/LegalSpec.groovy

```groovy
package gebwebbot.greach.legal

import geb.Browser
import spock.lang.Specification

class LegalSpec extends Specification {

    def "we are able to fetch the legal information about greach"() {

        when:
        def browser = new Browser()
        browser.go 'http://2017.greachconf.com'

        then:
        browser.page.find('footer div.credits').text() == 'The Greach Network SL, 2011-2017 - CIF B86412491 - C/Valtravieso, 28023 Madrid (Spain)'
    }
}
```

We are going to create a Geb.Config file to leverage Geb environment sensitivity.

vi src/test/resources/GebConfig.groovy

```groovy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver

environments {
    firefox {
        driver = { new FirefoxDriver() }
    }
    phantomJs {
        driver = { new PhantomJSDriver() }
    }
    chrome {
        driver = { new ChromeDriver() }
    }
}
```

Pass Java system properties from the command-line to the gradle test task. In order to do that modify our build.gradle file as follows:

<!-- [code language="groovy" highlight="19,20,21"] -->

```groovy
apply plugin: 'groovy'

repositories {
    jcenter()
}
ext {
    seleniumVersion = '2.52.0'
    phantomJsDriverVersion = '1.2.1'
}
dependencies {
    compile "org.gebish:geb-core:1.0"
    compile "org.seleniumhq.selenium:selenium-support:${seleniumVersion}"
    compile "org.seleniumhq.selenium:selenium-firefox-driver:${seleniumVersion}"
    compile "org.seleniumhq.selenium:selenium-chrome-driver:${seleniumVersion}"
    compile("com.codeborne:phantomjsdriver:$phantomJsDriverVersion")
    compile 'org.spockframework:spock-core:1.0-groovy-2.4'
}

test {
    systemProperties System.properties
}
```

Execute tests with firefox

```bash
./gradlew -Dgeb.env=firefox test
```

Execute tests with PhantomJS

You will need to download the drivers for your operating system in the PhantomJs Website.

```bash
./gradlew -Dgeb.env=phantomJs -Dphantomjs.binary.path=/Users/groovycalamari/Documents/phantomjs-2.1.1-macosx/bin/phantomjs test
```

Execute tests with Chrome

You will need to download the WebDriver for Chrome.

```bash
./gradlew -Dgeb.env=chrome -Dwebdriver.chrome.driver=/Users/groovycalamari/Documents/chromedriver test
```
