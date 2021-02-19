---
title: Grails Programmer : How to unit tests allowed HTTP request methods in a Grails 3 Controller's action?
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2016-09-28T07:55:00+01:00
date_modified: 2016-09-28T07:55:00+01:00
---

# [%title]

[%author.name] [%date_published]

Tags: #spock #grails

I am fan of restricting my Grails 3 Controller's actions to certain HTTP methods.

If an action does not modify the database let use GET, if an action inserts a new row in the database lets use POST, if an action deletes a row form the database lets use DELETE and so on.

With Grails allowedMethods it is easy to limit, based on the HTTP request method, the access to a controller's action.

Grails allowedMethods documentation:

The allowedMethods property provides a simple declarative syntax to specify which HTTP methods are allowed for your controller actions. By default, all request methods are allowed for all controller actions. The allowedMethods property is optional and only needs to be defined if the controller has actions to be restricted to certain request methods.

We want to restrict the index action of a controller to only POST requests.

```groovy
package myapp

import static org.springframework.http.HttpStatus.OK

class TestController {

    static allowedMethods = [index: 'POST']

    def index() {
        render text:"OK", status: OK
    }
}
```

And this is the unit test.

With the aim of separating what am I testing, I normally name these unit tests ControllerName+AllowedMethodsSpec. But you can name your unit test class whatever your want.

```groovy

package myapp

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

import static javax.servlet.http.HttpServletResponse.*

@TestFor(TestController)
class TestControllerAllowedMethodsSpec extends Specification {

    @Unroll
    def "test TestController.index does not accept #method requests"(String method) {
        when:
        request.method = method
        controller.index()

        then:
        response.status == SC_METHOD_NOT_ALLOWED

        where:
        method &amp;lt;&amp;lt; ['PATCH', 'DELETE', 'GET', 'PUT']
    }

    def "test TestController.index accepts POST requests"() {
        when:
        request.method = 'POST'
        controller.index()

        then:
        response.status == SC_OK
    }
}
```
