---
title: Github activity CLI updated to Micronaut 3.1
external_url: https://github.com/sdelamo/githubactivity
summary: I have updated the CLI application, which I use to check my Github activity to Micronaut 3.1.
date_published: 2021-10-16T12:35:06+01:00
keywords:micronaut
---

# [%title]

I have updated the [Github Activity CLI](https://github.com/sdelamo/githubactivity), the command line tool which I use to check my Github activity, to Micronaut 3.1.

The update is simple:  

- [Update to 7.1.0](https://github.com/sdelamo/githubactivity/commit/5a6f7bfc81740bb303e61c5861d822e98d95a43b) the [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow), a Gradle plugin for collapsing all dependencies and project code into a single Jar file.
- [Update to 2.0.6](https://github.com/sdelamo/githubactivity/commit/a987614ee7a50a044b3d7a94ad3cfe1ccadeed49) the [Micronaut Application Gradle Plugin](https://plugins.gradle.org/plugin/io.micronaut.application).
- [Set Micronaut version to 3.1.0](https://github.com/sdelamo/githubactivity/commit/2fe6568b7a9393cd1c97fd1df193e0fff90694d1).
- [Bump up gradle to 7.2](https://github.com/sdelamo/githubactivity/commit/90b59952eee6b2ef3eb10f40f3575ba679b19c43) with command `./gradlew wrapper --gradle-version=7.2`
