---
title: Git commit Hash in Gradle build file
summary: The following snippet shows how to get the Git commit hash in a Gradle build file using the Gradle Kotlin DSL.
date_published: 2025-03-26T14:20:50+01:00
keywords:gradle,git
---

# [%title]

[%summary]


```kotlin
fun String.execute(): String {
    val process = ProcessBuilder(*this.split(" ").toTypedArray())
        .directory(project.rootDir)
        .redirectErrorStream(true)
        .start()
    return process.inputStream.bufferedReader().readText().trim()
}
val commitHash = "git rev-parse --verify HEAD".execute()
```


