---
title: Groovy Programmer : How to use Groovy default parameter values to implement retry functionality
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png
date_published: 2016-11-16T17:16:00+01:00
date_modified: 2016-11-16T17:16:00+01:00
---

# [%title]

[%author.name] [%date_published]

Tags: #groovy

Lets say you have a class which depends on another component. The component ( for example a remote call ) sometimes fails. You want to execute a method with a number of retries. Groovy supports parameters with default values and we are going to leverage that capability and recursion to achieve a retry functionality.

```groovy
package groovycalamari

class WithRetryService {

    def remoteService

    String name(int retryAttempts = 2, int waitingMillisecondsBetweenAttempts = 5_000) {
        String result = remoteService?.name()
        if ( !result &amp;&amp; retryAttempts &gt; 0 ) {
            sleep(waitingMillisecondsBetweenAttempts)
            return name( (retryAttempts - 1), waitingMillisecondsBetweenAttempts )
        }
        result

    }
}
```

I have a collaborator which always fails.

```groovy
package groovycalamari

class AlwaysFailsRemoteService {
    int numberOfTimesCalled = 0

    String name() {
        numberOfTimesCalled++
        return null
    }
}
```

I've a collaborator which fails the first two times, but works the third time is invoked.

```groovy
package groovycalamari

class FailsTwiceWorksThridTimeRemoteService {

    int numberOfTimesCalled = 0

    String name() {
        if (numberOfTimesCalled &lt; 2) {
            numberOfTimesCalled++
            return null

        }
        'Sergio del Amo'
    }
}

```

And here the Spock test to verify it works:

```groovy
package groovycalamari

import spock.lang.Specification

class WithRetryServiceSpec extends Specification {

    def "test retry with a failing service will eventually fail"() {

        given:
        def service = new WithRetryService()
        service.remoteService = new AlwaysFailsRemoteService()

        when:
        String result = service.name()

        then:
        !result
        service.remoteService.numberOfTimesCalled == 3
    }

    def "test retry with a service which fails initially will eventually pass"() {

        given:
        def service = new WithRetryService()
        service.remoteService = new FailsTwiceWorksThridTimeRemoteService()

        when:
        String result = service.name()

        then:
        result
        service.remoteService.numberOfTimesCalled == 2
    }

}``



