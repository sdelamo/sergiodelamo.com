---
title: Add Google Analytics snippet to multiple .html files recursively with a Groovy Script
date_published: 2018-01-29T09:27:00+01:00
date_modified: 2018-01-29T09:27:00+01:00
---

# [%title]

[%date_published]

Tags: #groovy

Scenario: You have a static HTML website. With many .html files. You want to add the Google Analytics tracking snippet to every file. It is easy to create a Groovy script do it. Let me show you.

Create a build.gradle file which uses the [Groovy Gradle plugin](https://docs.gradle.org/current/userguide/groovy_plugin.html) and the [Gradle Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html).

```groovy
apply plugin: 'groovy'
apply plugin: 'application'

mainClassName = "demo.Main"

repositories {
    mavenCentral()
}

dependencies {
    compile 'org.codehaus.groovy:groovy-all:2.4.13'
}
```

```bash
$ mkdir -p src/main/groovy/demo
$ touch src/main/groovy/demo/Main.groovy
```

Create a file src/main/groovy/demo/Main.groovy

```groovy
package demo
import static groovy.io.FileType.FILES

class Main {
    public static void main(def args) {
        final String path ='/Users/sdelamo/website'
        if ( !new File(path).exists() ) {
            println "${path} does not exist"
	    return
	}
        final String code = 'UA-XXXXX-2'
        final String codePreffix = 'UA-'
        final String analyticsSnippet = """&lt;!-- Global site tag (gtag.js) - Google Analytics --&gt;
&lt;script async src="https://www.googletagmanager.com/gtag/js?id=${code}"&gt;&lt;/script&gt;
&lt;script&gt;
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', '${code}');
&lt;/script&gt;""".toString()

        final String fileSuffix = '.html'
        addGoogleAnalyticsSnippet(path, fileSuffix, codePreffix, analyticsSnippet)
    }

    private
    static addGoogleAnalyticsSnippet(String path, String fileSuffix, String codePreffix, String analyticsSnippet) {
        new File(path).eachFileRecurse(FILES) {
            if (it.name.endsWith(fileSuffix)) {
                if (!(it.text.contains(codePreffix))) {
                    println "adding analytics snippet to ${it.name}"
                    String text = it.text
                    it.text = text.replaceAll('&lt;head&gt;', "&lt;head&gt;\n${analyticsSnippet}".toString())
                } else {
                    println "${it.name} already contains Google analytics snippet."
                }
            }
        }
    }

}
```

Add gradlew wrapper

```bash
$ gradle wrapper
```

Run your script; Gradle run task is provided by the Gradle Application plugin

```bash
$ ./gradlew run
```
