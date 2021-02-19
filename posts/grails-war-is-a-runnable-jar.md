---
title: Grails War is a runnable JAR
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2017-07-28T14:39:00+01:00
date_modified: 2017-07-28T14:39:00+01:00
---

# [%title]

[%author.name] [%date_published]

Tags: #grails

Do you know you can run a Grails generated WAR with `gradle assemble` with `java -jar`?. Let me show you:

```bash
runnablewar$ grails --version
| Grails Version: 3.3.0
| Groovy Version: 2.4.11
| JVM Version: 1.8.0_121
runnablewar$ grails create-app --inplace
runnablewar$ ./gradlew assemble
:compileJava NO-SOURCE
:compileGroovy
:findMainClass
:assetCompile
Processing File 2 of 25 - apple-touch-icon.png
Processing File 1 of 25 - apple-touch-icon-retina.png
Processing File 3 of 25 - favicon.ico
Processing File 4 of 25 - grails-cupsonly-logo-white.svg
Processing File 5 of 25 - skin/database_add.png
Processing File 6 of 25 - skin/database_delete.png
Processing File 7 of 25 - skin/database_edit.png
Processing File 8 of 25 - skin/database_save.png
Processing File 9 of 25 - skin/database_table.png
Processing File 10 of 25 - skin/exclamation.png
Processing File 11 of 25 - skin/house.png
Processing File 13 of 25 - skin/shadow.jpg
Processing File 12 of 25 - skin/information.png
Processing File 14 of 25 - skin/sorted_asc.gif
Processing File 15 of 25 - skin/sorted_desc.gif
Processing File 16 of 25 - spinner.gif
Processing File 17 of 25 - application.js
Processing File 18 of 25 - bootstrap.js
Processing File 19 of 25 - jquery-2.2.0.min.js
Processing File 20 of 25 - application.css
Processing File 21 of 25 - bootstrap.css
Processing File 22 of 25 - errors.css
Processing File 23 of 25 - grails.css
Processing File 24 of 25 - main.css
Processing File 25 of 25 - mobile.css
Finished Precompiling Assets
:buildProperties
:processResources
:classes
:compileWebappGroovyPages NO-SOURCE
:compileGroovyPages
:war
:bootRepackage
:assemble

BUILD SUCCESSFUL

Total time: 17.741 secs
runnablewar$ java -jar build/libs/runnablewar-0.1.war
Grails application running at http://localhost:8080 in environment: production
```
