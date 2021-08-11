---
title: Gradle Programmer - Ensure source and target compatibility to a specific version of Java
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-06-17T06:27:00+01:00
date_modified: 2016-06-17T06:27:00+01:00
keywords:gradle
---

# [%title]

I originally saw this tip in an [Andrés Almiray](https://twitter.com/aalmiray)'s talk. There is a [Gradle forum thread](https://discuss.gradle.org/t/enforcing-targetcompatibility-when-compiling-java-code-with-the-groovy-plugin/5065/1) where it is discussed too.

Sometimes you want to run your program in a machine with Java 1.6. How to ensure source and target compatibility in a `build.gradle` file:

```groovy
apply plugin: 'java'
apply plugin: 'groovy'

repositories {
    mavenCentral()
}

sourceCompatibility = 1.6
targetCompatibility = 1.6

dependencies {
    compile 'org.codehaus.groovy:groovy-all:2.4.7'
}

tasks.withType(JavaCompile) {
    sourceCompatibility = '1.6'
    targetCompatibility = '1.6'
}
```
