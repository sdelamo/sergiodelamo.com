---
title: How to ignore a Spock feature method in Grails 3 while running on Jenkins
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-05-02T09:32:00+01:00
date_modified: 2016-05-02T09:32:00+01:00
---

# [%title]

[%date_published]

Tags: #grails #spock

It is easy to ignore a test only if the test is run in a particular environment thanks to the @IgnoreIf Spock annotation. If you run your tests in Jenkins, it is probable that the tests are being run by a user called jenkins. Thus, you can ignore a test in a Jenkins Job easily:

```groovy
@Integration(applicationClass = Application.class)
@Rollback
class IntegrationSpec extends Specification {

    @IgnoreIf({ System.getProperty("user.name") == 'jenkins' })
    def "test will be ignored in jenkins"() {
        expect:
        false
    }
}
```