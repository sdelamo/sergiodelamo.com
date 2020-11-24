---
title: Spock Programmer : Setup / Cleanup Inheritance
date_published: 2016-08-21T06:24:00+01:00
date_modified: 2016-08-21T06:24:00+01:00
---

# [%title]

[%date_published]

Tags: #spock

Craig Atkinson's talk at GR8Conf US Intro to Spock and Geb claimed the next about Spock Cleanup / Inheritance.

Might have a hierarchy of test classes for DRY common test code
Spock runs the base class setup first
Then goes down the inheritance chain
cleanup is the reverse, starting at test class then going up to base class
I want it to double test to make sure I understood.

```groovy
import spock.lang.Specification

abstract class BaseSpec extends Specification {
    def setup() { println 'base setup()' }
    def cleanup() { println 'base cleanup()' }
}

class DerivedSpec extends BaseSpec {
    def setup() { println 'derived setup()' }
    def cleanup() { println 'derived cleanup()' }

    def "test setup / cleanup inheritance"() {
        when:
        println "exectuting test case"
        then:
        true
    }
}
```

Running the above test prints:

```
base setup()
derived setup()
exectuting test case
derived cleanup()
base cleanup()
```

Really nice. Spock Setup / Cleanup inheritance to aid the creation of a hierarchy of tests classes.
