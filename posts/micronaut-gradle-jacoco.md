---
title: Gradle, Jacoco and 90% Code Coverage
summary: With Jacoco Gradle plugin it is easy to setup your project to enforce a coverage percentage.
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-04-29T21:24:32+01:00
date_modified: 2021-04-29T21:24:32+01:00
---

# [%title]


By [%author.name] - [%date_published]

Tags: #test #gradle #jacoco

With [Jacoco Gradle Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
 it is easy to setup your project to enforce a coverage percentage.

I use the following configuration to enforce 90% both at class and at project level. 

```groovy
plugins {
    ...
	id('jacoco')
}

...
..
.

check {
	dependsOn jacocoTestCoverageVerification
}
jacocoTestReport {
	dependsOn test
}
jacocoTestReport {
	reports {
		xml.enabled false
		csv.enabled false
	}
}
jacocoTestCoverageVerification {
    violationRules {
	    rule {
		    limit {
			    minimum = 0.9
		    }
	    }
	    rule {
		    element = 'CLASS'
		    excludes = ['example.micronaut.Application']
		    limit {
			    minimum = 0.9
		    }
	    }
    } 
}
```

The above example excludes the `Application` class. 

Yes, I use Apache Groovy Gradle DSL. 