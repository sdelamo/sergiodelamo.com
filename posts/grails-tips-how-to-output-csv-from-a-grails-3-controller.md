---
title: Grails Programmer : How to output CSV from a Grails 3  Controller
date_published: 2019-09-20T06:46:00+01:00
date_modified: 2019-09-20T06:46:00+01:00
---

# [%title]

[%date]

Tags: #grails

```bash
% grails -version
Grails Version: 3.1.11
| Groovy Version: 2.4.7
| JVM Version: 1.8.0_45
```

Create an app with the default web profile.

```bash
grails create-app csv
Application created at /Users/groovycalamari/Documents/tests/csv
```

Enter the grails console

```bash
% cd csv
% grais
```

Create a domain class

```bash
grails> create-domain-class Book
| Created grails-app/domain/csv/Book.groovy
| Created src/test/groovy/csv/BookSpec.groovy
```

Add a couple of properties to the domain class

```groovy
package cvs

class Book {

	String title
	String author

    static constraints = {
    }
}
```

In grails-app/init/BootStrap.groovy add a couple of domain class instances. The BootStrap init closure runs when the app starts.

```groovy
import csv.*

class BootStrap {
    def init = { servletContext -&amp;gt;
	new Book(title: 'Groovy for Domain-Specific Languages', author: 'Fergal Dearle').save()
	new Book(title: 'Programming Groovy 2: Dynamic Productivity for the Java Developer', author: 'Venkat Subramaniam').save()
    }
    def destroy = {
    }
}
```

Create a controller

```
$ grails
Enter a command name to run. Use TAB for completion:e...
grails&amp;gt; create-controller Book
| Created grails-app/controllers/csv/BookController.groovy
| Created src/test/groovy/csv/BookControllerSpec.groovy
```

This is the controller content:

```groovy
package csv

import grails.config.Config
import grails.core.support.GrailsConfigurationAware

import static org.springframework.http.HttpStatus.OK

class BookController implements GrailsConfigurationAware {

    String csvMimeType

    String encoding

    def index() {
    	final String filename = 'book.csv'
        def lines = Book.findAll().collect { [it.title, it.author].join(';') } as List&amp;lt;String&amp;gt;

        def outs = response.outputStream
        response.status = OK.value()
        response.contentType = "${csvMimeType};charset=${encoding}";
        response.setHeader "Content-disposition", "attachment; filename=${filename}"

        lines.each { String line -&amp;gt;
            outs &amp;lt;&amp;lt; "${line}\n"
        }

        outs.flush()
        outs.close()
    }

    @Override
    void setConfiguration(Config co) {
        csvMimeType = co.getProperty('grails.mime.types.csv', String, 'text/csv')
        encoding = co.getProperty('grails.converters.encoding', String, 'UTF-8')

    }
}
```

Several things about the above code.

A) I will recommend to put the logic fetching the lines in a Service.
B) I am using the mime type and encoding defined in application.yml. Learn more about retrieving config values.
C) If you want the file to download you need to setup the Content-disposition header.

If we run the app and call the controller we will download a CSV file as this:

CSV is probably the best format to export your data from a Grails App. A CSV file is easy to import in Excel. I was tired of my clients asking me how to import a CSV in Excel. I wrote a post; in Spanish though.

