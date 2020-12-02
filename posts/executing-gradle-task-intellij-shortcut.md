---
title: Execute a Gradle task shortcut with IntelliJ IDEA
summary: How to setup a shortcut to execute a Gradle task with IntelliJ IDEA
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2020-11-30T12:40:21+01:00
date_modified: 2020-11-30T12:40:21+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #gradle #intellijidea #automation

Most of my projects are Gradle Builds. For such projects, I use in IntelliJ IDEA. However, I delegate Build and Run and tests to Gradle. You can do that at `Settings/Preferences | Build, Execution, Deployment |Build Tools | Gradle`.

Thus, running tasks such as test, run, clean or build means executing a Gradle task. Because of that I have a shortcut to execute a Gradle task within IntelliJ IDEA:

![Custom shortcut to execute a Gradle task in IntelliJ IDEA](https://images.sergiodelamo.com/execute-gradle-task-shortcut-intellij.png)

Now when I type `CONTROL + OPTION + COMMAND + G`, I can run the any Gradle task:

![Execute a Gradle task in IntelliJ IDEA](https://images.sergiodelamo.com/execute-gradle-task-shortcut-intellij-running.png)