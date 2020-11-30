---
title: How to log SQL statements in a Grails 3 app
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-02-24T10:20:00+01:00
date_modified: 2016-02-24T10:20:00+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #grails

```
| Grails Version: 3.1.1
| Groovy Version: 2.4.5
| JVM Version: 1.8.0_45
$ grails create-app myapp
| Application created at /Users/shoptimix/Documents/tests/springsecurityexample/myapp
$ cd myapp
$ grails
grails&gt; create-domain-class ProductAnnouncement
| Created grails-app/domain/myapp/ProductAnnouncement.groovy
| Created src/test/groovy/myapp/ProductAnnouncementSpec.groovy
```

I add a message field to my domain class since I need to announce stuff.

```groovy
package myapp

class ProductAnnouncement {
    String message

    static constraints = {
    }
}
```

Add some ProductAnnouncement messages when the application starts.
To do that modify grails-app/init/BootStrap.groovy

```groovy
import myapp.*

class BootStrap {

    def init = { servletContext -&gt;
	new ProductAnnouncement(message: 'Launch day').save()
    }

    def destroy = {
    }
}
```

Now If I start the app, I don't see any SQL output. I guess the .save() call did an insert but I have no visibility.

```
| Running application...
Grails application running at http://localhost:8080 in environment: development
```

How can I log the generated SQL statement?

It is easy. You just need to modify grails-app/conf/application.yml and add a logSql:true line

<!--[code highlight="19" language="groovy"]-->
```groovy
---
hibernate:
    cache:
        queries: false
        use_second_level_cache: true
        use_query_cache: false
        region.factory_class: 'org.hibernate.cache.ehcache.EhCacheRegionFactory'

dataSource:
    pooled: true
    jmxExport: true
    driverClassName: org.h2.Driver
    username: sa
    password:

environments:
    development:
        dataSource:
            logSql: true
            dbCreate: create-drop
            url: jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    test:
        dataSource:
            dbCreate: update
            url: jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    production:
        dataSource:
            dbCreate: update
            url: jdbc:h2:./prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
            properties:
                jmxEnabled: true
                initialSize: 5
                maxActive: 50
                minIdle: 5
                maxIdle: 25
                maxWait: 10000
                maxAge: 600000
                timeBetweenEvictionRunsMillis: 5000
                minEvictableIdleTimeMillis: 60000
                validationQuery: SELECT 1
                validationQueryTimeout: 3
                validationInterval: 15000
                testOnBorrow: true
                testWhileIdle: true
                testOnReturn: false
                jdbcInterceptors: ConnectionState
                defaultTransactionIsolation: 2 # TRANSACTION_READ_COMMITTED

---
---
grails:
    profile: web
    codegen:
        defaultPackage: myapp
    spring:
        transactionManagement:
            proxies: false
info:
    app:
        name: '@info.app.name@'
        version: '@info.app.version@'
        grailsVersion: '@info.app.grailsVersion@'
spring:

    groovy:
        template:
            check-template-location: false

---
grails:
    mime:
        disable:
            accept:
                header:
                    userAgents:
                        - Gecko
                        - WebKit
                        - Presto
                        - Trident
        types:
            all: '*/*'
            atom: application/atom+xml
            css: text/css
            csv: text/csv
            form: application/x-www-form-urlencoded
            html:
              - text/html
              - application/xhtml+xml
            js: text/javascript
            json:
              - application/json
              - text/json
            multipartForm: multipart/form-data
            pdf: application/pdf
            rss: application/rss+xml
            text: text/plain
            hal:
              - application/hal+json
              - application/hal+xml
            xml:
              - text/xml
              - application/xml
    urlmapping:
        cache:
            maxsize: 1000
    controllers:
        defaultScope: singleton
    converters:
        encoding: UTF-8
    views:
        default:
            codec: html
        gsp:
            encoding: UTF-8
            htmlcodec: xml
            codecs:
                expression: html
                scriptlets: html
                taglib: none
                staticparts: none
endpoints:
    jmx:
        unique-names: true
```

Now when you run the app, it will log the SQL statements:

```
$ grails run-app
| Running application...
Hibernate: drop table product_announcement if exists
Hibernate: create table product_announcement (id bigint generated by default as identity, version bigint not null, message varchar(255) not null, primary key (id))
Hibernate: insert into product_announcement (id, version, message) values (null, ?, ?)
Grails application running at http://localhost:8080 in environment: development
```

if you modify your hibernate block in grails-app-/conf/application.yml as follows:

<!-- [code highlight="3"] -->
```yaml
---
hibernate:
    format_sql: true
    cache:
        queries: false
        use_second_level_cache: true
        use_query_cache: false
        region.factory_class: 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
```

You output will look much more readable:

```
Hibernate:
    drop table product_announcement if exists
Hibernate:
    create table product_announcement (
        id bigint generated by default as identity,
        version bigint not null,
        message varchar(255) not null,
        primary key (id)
    )
Hibernate:
    insert
    into
        product_announcement
        (id, version, message)
    values
        (null, ?, ?)
Grails application running at http://localhost:8080 in environment: development
```

Do you want to see values instead of question marks?

Modify `grails-app/conf/logback.groovy`:

<!-- [code language="groovy" highlight="11, 12"] -->

```groovy
import grails.util.BuildSettings
import grails.util.Environment

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

logger 'org.hibernate.type.descriptor.sql.BasicBinder', TRACE, ['STDOUT']
logger 'org.hibernate.SQL', TRACE, ['STDOUT']

root(ERROR, ['STDOUT'])

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() &amp;&amp; targetDir) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
```

And if you rerun the app you will see:

```
DEBUG org.hibernate.SQL -
    insert
    into
        product_announcement
        (id, version, message)
    values
        (null, ?, ?)
Hibernate:
    insert
    into
        product_announcement
        (id, version, message)
    values
        (null, ?, ?)
TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [1] as [BIGINT] - [0]
TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [2] as [VARCHAR] - [Launch day]
Grails application running at http://localhost:8080 in environment: development
```

The last part of this post is improved thanks to the post Grails Hibernate Logging by Dan Vega

Logging SQL can help you to optimise single queries and also it will warn you if you execute to many queries to the database while performing a single task.

