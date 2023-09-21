---
title: Method to check if GraalVM JDK Distribution
summary: Method to check if you are running in [GraalVM](https://www.graalvm.org) JDK distribution. I have used it often in Gradle build files to decide whether a [Gradle task](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:locating_tasks) should be enabled. 
date_published: 2023-09-21T18:08:30+01:00
keywords:java,graalvm,gradle
---
# [%title]

[%summary]

```java
private static boolean isGraalVMJava() {
    return Arrays.asList("jvmci.Compiler", "java.vendor.version", "java.vendor")
            .stream()
            .anyMatch(propertyName -> {
                String value = System.getProperty(propertyName);
                return value != null && value.toLowerCase(Locale.ENGLISH).contains("graal");
            });
}
```