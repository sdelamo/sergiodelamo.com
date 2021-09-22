---
title: Test a Grails Application with Micronaut HTTP Client
summary: A parent class which I use for the tests which verify a Grails application API
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-09-22T14:45:53+01:00
date_modified: 2021-09-22T14:45:53+01:00
keywords:grails,micronaut
---
 
 # [%title]
 
To use a [Micronaut HTTP Client](https://docs.micronaut.io/latest/guide/#httpClient) to test the API exposed by your Grails application you can use the following parent class for your integration tests:

```groovy
package example

import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

abstract class ClientSpec extends Specification {

    @Shared
    HttpClient _httpClient

    @Shared
    BlockingHttpClient _client

    HttpClient getHttpClient() {
        if (_httpClient == null) {
            _httpClient = createHttpClient()
        }
        _httpClient
    }

    BlockingHttpClient getClient() {
        if (_client == null) {
            _client = getHttpClient().toBlocking()
        }
        _client
    }

    HttpClient createHttpClient() {
        String baseUrl = "http://localhost:$serverPort"
        HttpClient.create(baseUrl.toURL())
    }

    def cleanupSpec() {
        resetHttpClient()
    }

    void resetHttpClient() {
        _httpClient?.close()
        _httpClient = null
        _client = null
    }
}
```

A example test in `src/integrationTest/groovy`: 

```groovy
@Integration
class ApiContactControllerSpec extends ClientSpec {

    void "/apiContact is not a valid path"() {
        when:
        client.exchange(HttpRequest.GET('/apiContact'))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.NOT_FOUND
    }
}
```

You will need the following dependencies in `build.gradle`: 

```groovy
...
dependencies {
    ...
    ..
    testImplementation "io.micronaut:micronaut-inject-groovy"
    testImplementation "io.micronaut:micronaut-http-client"
}
```