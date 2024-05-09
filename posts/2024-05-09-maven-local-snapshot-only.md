---
title: MavenLocal repository only for snapshots with the Gradle Kotlin DSL
summary: Sometimes, while developing you need to [define mavenLocal() repository](https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:case-for-maven-local) in a Gradle build.  Gradle allows you to constraint the repository only to snapshots.
date_published: 2024-05-09T09:55:11+01:00
keywords:gradle,kotlin
---

# [%title]

[%summary]

It is easy to do it with the Gradle Kotlin DSL. 

```
repositories {
    mavenCentral()
    maven {
        setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
}
```
