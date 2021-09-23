---
title: Grails Programmer : How to log from a none Grails Artifact
date_published: 2016-09-23T08:13:00+01:00
keywords:grails
---

# [%title]

Create a grails app with the rest profile:

```
$ grails create-app lognongrailsartifact --profile=rest-api
```

Create a controller grails-app/controllers/lognongrailsartifact/TestController.groovy

```groovy
package lognongrailsartifact

import static org.springframework.http.HttpStatus.OK

class TestController {

    def index() {
    	new NonGrailsArtifact().greet()

        render text:"OK", status: OK

    }
}
```

Create a Groovy Class src/main/groovy/lognongrailsartifact/NonGrailsArtifact.groovy; not a grails artifact.

Note: Grails artefacts get log auto-injected. You will need to inject it manually in this class.

```groovy
package lognongrailsartifact

import org.apache.commons.logging.LogFactory

class NonGrailsArtifact {
	private static final log = LogFactory.getLog(this)

	void greet() {
		log.info 'Hi'
	}
}
```

Edit `grails-app/conf/logback.groovy`

```groovy
import grails.util.BuildSettings
import grails.util.Environment

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

root(ERROR, ['STDOUT'])
logger 'lognongrailsartifact.NonGrailsArtifact', INFO, ['STDOUT']

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() &amp;amp;&amp;amp; targetDir) {
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

Run the app

```
grails&amp;gt; run-app
| Running application...
Grails application running at http://localhost:8080 in environment: development
```

Invoke the controller

```
$ curl -X "GET" "http://localhost:8080/test" -H "Accept: application/json"
```

And you will see the log statement:

```
grails&amp;gt; INFO lognongrailsartifact.NonGrailsArtifact - Hi
```

Soeren Glasius pointed me to a more elegant way to it with an AST.

```groovy
package lognongrailsartifact

import groovy.util.logging.Log4j

@Log4j
class NonGrailsArtifact {
    void greet() {
        log.info 'Hi'
    }
}
```

An even better approach, as pointed by Alvaro Sanchez, will be to do it with @Slf4j.

```
package lognongrailsartifact

import groovy.util.logging.Slf4j

@Slf4j
class NonGrailsArtifact {
    void greet() {
        log.info 'Hi'
    }
}
```

SLF4J vs LOG4J Which one to prefer?. SLF4J is basically an abstraction layer. It is not a logging implementation. It means that if you're writing a library and you use SLF4J, you can give that library to someone else to use and they can choose which logging implementation to use with SLF4J e.g. log4j or the Java logging API

