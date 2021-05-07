---
title: Test Micronaut Health endpoint
summary: I always expose the /health endpoint in my Micronaut applications. It is easy to test it.
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-05-06T07:05:17+01:00
date_modified: 2021-05-06T07:05:17+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #micronaut #test #spock 

[Micronaut management](https://docs.micronaut.io/latest/guide/#management) dependency adds support for monitoring of your application via endpoints. For example, the [health endpoint](https://docs.micronaut.io/latest/guide/#healthEndpoint) exposes the state of your application.

I test it with [Spock](https://spockframework.org):

```groovy
package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import javax.inject.Inject
import spock.lang.Specification
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient

@MicronautTest
class HealthSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient
    
    void "/health responds OK"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET('/health')
        
        when:
        HttpResponse<Map> response = client.exchange(request, Map)

        then:
        noExceptionThrown()
        response.status() == HttpStatus.OK
        response.body() == [status: 'UP']
    }
}
```

