title: Ignore a Spock test if running in Travis CI
date: Apr 27, 2017
---

# [%title]

[%date] #spock

Thanks to Spock IgnoreIf annotation it is easy to ignore a specification running in Travis CI.

```groovy
import spock.lang.IgnoreIf
import spock.lang.Specification

class ClassUnderTestSpec extends Specification {

    @IgnoreIf( { System.getenv('TRAVIS') as boolean } )
    def "test which fials if run by Travis CI"() {
    }
```