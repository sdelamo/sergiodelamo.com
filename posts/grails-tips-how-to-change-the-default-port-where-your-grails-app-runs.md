---
title: Grails Programmer : How to change the default port where your Grails App runs?
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-02-23T15:44:00+01:00
date_modified: 2016-02-23T15:44:00+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #grails

If you create a Grails 3 app and run it; it will start at port 8080.

```
$  grails --version
Picked up JAVA_TOOL_OPTIONS: -Dfile.encoding=UTF-8 -Duser.language=en -Duser.region=US
| Grails Version: 3.0.14
| Groovy Version: 2.4.5
| JVM Version: 1.8.0_45
$  grails create-app testnewport
| Application created at /Users/groovycalamari/Documents/tests/testnewport
$ cd testnewport
$ grails run-app
Grails application running at http://localhost:8080 in environment: development
```

If you want to start it at a different port add to your application.yml the next block:

```groovy
---
server:
 port: 28080
```

And voila:

```
Grails application running at http://localhost:28080 in environment: development
```
