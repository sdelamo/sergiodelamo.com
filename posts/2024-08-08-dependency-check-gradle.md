---
title: Dependency Check Gradle Plugin
summary: > The dependency-check gradle plugin allows projects to monitor dependent libraries for known, published vulnerabilities.
date_published: 2024-08-08T10:15:34+01:00
keywords:gradle,security,plugin
external_url: https://github.com/dependency-check/dependency-check-gradle
---

# [%title]

[%summary]

I requested a [NVD API Key](https://nvd.nist.gov/developers/request-an-api-key).

I set the NVD key as a π[global property for all Gradle Builds](https://blog.mrhaki.com/2015/10/gradle-goodness-setting-global.html). I added an entry to `USER_HOME/.gradle/gradle.properties`.

```properties 
nvdKey=xxxx-yyy-zz-xgb-xvfbbbb
```

Then, I can apply the plugin with:

```groovy
plugins {
    ...
    id("org.owasp.dependencycheck") version "10.0.3"
}
...
dependencyCheck {
    nvd {
        apiKey = "${nvdKey}"
    }
}
```

The first time I run the plugin it took me 30m.