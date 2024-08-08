---
title: Sonatype Scan Gradle Plugin
summary:  Sonatype offers plugins to check for vulnerabilities in your dependencies.
date_published: 2024-08-08T11:07:04+01:00
keywords:gradle,plugin,security
external_url: https://github.com/sonatype-nexus-community/scan-gradle-plugin/#readme
---

# [%title]

[%summary]

## Web Search

You can [search directly](https://ossindex.sonatype.org) in the web interface for vulnerabilities in your dependencies.

For example, to search for [`org.json:json`](https://ossindex.sonatype.org/component/pkg:maven/org.json/json) type: `pkg:maven/org.json/json`.

## Scan Your dependencies

Sonatype OSS Index offers [Maven](https://sonatype.github.io/ossindex-maven/maven-plugin/) and [Gradle](https://github.com/sonatype-nexus-community/scan-gradle-plugin/#readme) Plugins. I  focus next on Gradle.

### Registration

I signed up for [Sonatype OSS Index](https://ossindex.sonatype.org).

I set the OSS Index username/password as [global properties for all Gradle Builds](https://blog.mrhaki.com/2015/10/gradle-goodness-setting-global.html). I added entries to `USER_HOME/.gradle/gradle.properties`.

```properties 
ossIndexUsername=xxx@email.com
ossIndexPassword=yyyy
```

## Setup Gradle Plugin

You can get the [latest version of `org.sonatype.gradle.plugins.scan`in the Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.sonatype.gradle.plugins.scan )

Then, you can apply the plugin:

```kotlin
plugins {
...
id("org.sonatype.gradle.plugins.scan") version "2.8.2"
}

dependencies {
    ....
    ..
    .
}
ossIndexAudit {
    username = project.properties["ossIndexUsername"].toString()
    password = project.properties["ossIndexPassword"].toString()
}
```

If your project has a vulnerable dependency, when execute the gradle task `ossIndexAudit`  will see something like this:

```
> Task :ossIndexAudit FAILED
  ________  ___   ___  __   ____  ____________   _  __
 / ___/ _ \/ _ | / _ \/ /  / __/ / __/ ___/ _ | / |/ /
/ (_ / , _/ __ |/ // / /__/ _/  _\ \/ /__/ __ |/    /
\___/_/|_/_/ |_/____/____/___/ /___/\___/_/ |_/_/|_/

  _      _                       _   _
 /_)    /_`_  _  _ _/_   _  _   (/  /_`_._  _   _/ _
/_)/_/ ._//_// //_|/ /_//_//_' (_X /  ///_'/ //_/_\
   _/                _//
Gradle Scan version: 2.8.2
------------------------------------------------------------------------------------------------------------------------------------------------------

Checking vulnerabilities in 52 dependencies
Found vulnerabilities in 1 dependencies
[1/1] - pkg:maven/org.json/json@20230618 - 1 vulnerability found!

   Vulnerability Title:  [CVE-2023-5072] CWE-770: Allocation of Resources Without Limits or Throttling
   ID:  CVE-2023-5072
   Description:  Denial of Service  in JSON-Java versions up to and including 20230618. Â A bug in the parser means that an input string of modest size ca...
   CVSS Score:  (7.5/10, High)
   CVSS Vector:  CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H
   CVE:  CVE-2023-5072
   Reference:  https://ossindex.sonatype.org/vulnerability/CVE-2023-5072?component-type=maven&component-name=org.json%2Fjson&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.2


Execution failed for task '::ossIndexAudit'.
> Vulnerabilities detected, check log output to review them
```

The plugin scans the whole dependency tree. It scans not just your project dependencies but the dependencies of those dependencies.