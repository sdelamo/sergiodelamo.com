---
title:  Configure a Gradle Kotlin DSL to resolve Micronaut Snapshots
summary: [Micronaut documentation describes how to use snapshots](https://docs.micronaut.io/4.7.10/guide/#usingsnapshots).
date_published: 2025-01-07T11:17:26+01:00
keywords:micronaut,gradle
---

# [%title]

[%summary]

The following snippet, which uses the [Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html), allows you to resolve Micronaut snapshots:

```kotlin 
repositories {
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
    mavenCentral {
        mavenContent {
            releasesOnly()
        }
    }
}
```