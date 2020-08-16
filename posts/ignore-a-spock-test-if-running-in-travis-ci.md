title: Ignore a Spock test if running in Travis CI
date: Apr 27, 2017, 08:24
---

# [%title]

[%date]

Tags: #ci #travis #spock

Thanks to [Spock IgnoreIf annotation](http://mrhaki.blogspot.com.es/2014/06/spocklight-ignore-specifications-based.html) it is easy to ignore a specification running in Travis CI.

```groovy
import spock.lang.IgnoreIf
import spock.lang.Specification

class ClassUnderTestSpec extends Specification {

    @IgnoreIf( { System.getenv('TRAVIS') as boolean } )
    def "test which fials if run by Travis CI"() {
    }
```