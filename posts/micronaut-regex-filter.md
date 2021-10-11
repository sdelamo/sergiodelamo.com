---
title: Micronaut Filter Regex
summary: Since Micronaut 3.1, you can use a regular expression in the HTTP server filter patterns
banner_image: https://images.sergiodelamo.com/micronaut-filter-regex.png
date_published: 2021-10-11T18:28:53+01:00
keywords:micronaut
---

# [%title]

[%summary].

Micronaut [HTTP Server filters](https://docs.micronaut.io/latest/guide/#filters) supported Ant-style path patterns. Since 3.1, they support also regular expressions. The default pattern style is Ant-style path pattern. 

## Example

Imagine you have an API, which returns a list of secrets. You use a url path variable to version your API. For example: `/api/v1/secrets`. 

My first version was a bit naive, I did not even returned JSON, but plain text instead:

```java
package com.example.api.v1;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.Collections;
import java.util.List;

@Controller("/api/v1")
class SecretController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/secret")
    List<String> index() {
        return Collections.singletonList("I admire Moriarty");
    }
}
```

for v2, I returned JSON:

```java
package com.example.api.v2;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Collections;
import java.util.Map;

@Controller("/api/v2")
public class SecretController {
    @Get("/secret")
    Map<String, Object> index() {
        return Collections.singletonMap("messages", 
            Collections.singletonList("I admire Moriarty"));
    }
}
``` 

For v3, I changed the JSON key from `messages` to `secrets`.

```java
package com.example.api.v3;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Collections;
import java.util.Map;

@Controller("/api/v3")
public class SecretsController {
    @Get("/secret")
    Map<String, Object> index() {
        return Collections.singletonMap("secrets", 
        Collections.singletonList("I admire Moriarty"));
    }
}
```

I want to keep `v1`, `v2` endpoints alive. However, I want to notify my consumers that they should migrate to `v3`.

I can add a [`Deprecation` HTTP Header](https://datatracker.ietf.org/doc/html/draft-dalal-deprecation-header-03) to the HTTP Responses emitted by `v1` and `v2` endpoints. Thanks to the support of regular expressions since Micronaut 3.1, it is really easy to do it: 

```java
package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterPatternStyle;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Filter(patternStyle = FilterPatternStyle.REGEX, 
        patterns = "/api/v(1|2)/.*")
public class DeprecatedApiVersionsFilter implements HttpServerFilter {

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return Flux.from(chain.proceed(request))
                .map(response -> response.header("Deprecation", "true"));
    }
}
```

The key in the above `Filter` is that I am defining my pattern style to be a regular expression with `patternStyle = FilterPatternStyle.REGEX` and then I can use a regular expression: `/api/v(1|2)/.*` as the pattern value. 

`/api/v(1|2)/.*` matches:

 ✅ `/api/v1/secrets`   
 ✅ `/api/v2/secrets`  
 ⭕️ `/api/v3/secrets`  

Regular expressions is an art. To work with regular expressions I use [Patterns for MacOS](https://krillapps.com/patterns/) or [RegexBuddy](https://www.regexbuddy.com) in Windows.


