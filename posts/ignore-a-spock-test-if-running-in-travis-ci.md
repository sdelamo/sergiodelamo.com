---
title: Ignore a Spock test if running in Travis CI
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2017-04-27T08:24:00+01:00
date_modified: 2017-04-27T08:24:00+01:00
keywords:ci,travis,spock
---

# [%title]

Thanks to [Spock IgnoreIf annotation](http://mrhaki.blogspot.com.es/2014/06/spocklight-ignore-specifications-based.html) it is easy to ignore a specification running in Travis CI.

```groovy
import spock.lang.IgnoreIf
import spock.lang.Specification

class ClassUnderTestSpec extends Specification {

    @IgnoreIf( { System.getenv('TRAVIS') as boolean } )
    def "test which fials if run by Travis CI"() {
    }
```