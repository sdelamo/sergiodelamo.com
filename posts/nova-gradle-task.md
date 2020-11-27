---
title: Run a Gradle build with Nova editor
date_published: 2020-09-22T11:07:00+01:00
date_modified: 2020-09-22T11:07:00+01:00
summary: How to run a Gradle build with Nova custom tasks
---

# [%title]

[%date_published]

Tags: #nova #gradle

I build this website with [Gradle](https://gradle.org). Posts are written in Markdown. When I run `./gradlew build`, I build a statically generated site, a collection of HTML, CSS, images, PDFs and javascript files.  I am trying [Nova](https://nova.app), the code editor recently announced by [Panic](https://panic.com). I've been a long time customer of Panic products [Coda 2](https://panic.com/coda/), [Transmit](https://panic.com/transmit/). Thus, I was been eagearly waiting for Nova. So far, I can tell you two things. It feels Mac native and it feels fast. 

I set some custom tasks to build my website. 

[Nova run Tasks](https://library.panic.com/nova/run-tasks/)

> Run Tasks provide a highly-configurable interface for running external operations from inside Nova. You can create your own Run Tasks, or they may be provided by Extensions you have installed.

![Build Gradle build with Nova](https://images.sergiodelamo.com/nova-app-gradle-build.png)

![Clean Gradle build with Nova](https://images.sergiodelamo.com/blog/nova-app-gradle-clean.png)

I wrote this blog post in Nova, build the site locally, commit the changes and push directly from the editor. 

![Clean and Build with Gradle and Nova](https://images.sergiodelamo.com/blog/NovaGradle.gif)