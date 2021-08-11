---
title: Grails Programmer: How to run a Grails 3 App with IntelliJ
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-10-28T07:49:00+01:00
date_modified: 2016-10-28T07:49:00+01:00
keywords:grails,gradle,idea
---

# [%title]

This post explains how to run a Grails 3 App with IntelliJ as described in the [Grails 3 IntelliJ Quickcast](https://player.vimeo.com/video/186362455#t=3m17s) by [Jeff Scott Brown](https://twitter.com/jeffscottbrown)

## Using Application.groovy

It is easy to run a Grails 3 App directly from IntelliJ. Execute the `main` method in `Application.groovy`. Just click the green arrow.


![](Application_groovy_-_music_-____Developer_tests_music_.png]

Please, make sure you [turn off bytecode verification](http://sergiodelamo.es/run-grails-3-app-from-intellij-with-runtime-reloading/) to get runtime reloading

## Run with Gradle BootRun Task

You can open a Terminal within IntelliJ and run the gradle task `bootRun`. Reloading agent will work using the Gradle wrapper.

![](https://images.sergiodelamo.com/Fullscreen_25_10_2016__08_22-1024x822.png)

### Run with Grails Run-app

You can execute `grails run-app` directly from a terminal you open within IntelliJ.

![](https://images.sergiodelamo.com/Fullscreen_25_10_2016__08_27.png)