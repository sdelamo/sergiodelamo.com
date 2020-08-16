title: Gradle Programmer - Ensure source and target compatibility to a specific version of Java
date: Jun 17, 2016 06:27
---

# [%title]

[%date]

Tags: #gradle

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
