---
title: SpringBoot to Micronaut - Controller annotations
summary: SpringBoot and Micronaut applications ease the creation of routes with similar annotations.
date_published: 2021-04-27T10:45:56+01:00
keywords:micronaut,springboot
---

# [%title]

[%summary]


[SpringBoot](https://spring.io/projects/spring-boot):


```java
package scorekeep;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
    public Map<String, Object> index(@PathVariable String gameId) {        
        return Collections.singletonMap("messages", "Hello World");
    }
}
```

[Micronaut](https:/micronaut.io):

```java
package scorekeep;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import java.util.Collections;
import java.util.Map;

@Controller("/api/score")
public class ScoreController {

    @Get( "/{gameId}")
    public Map<String, Object> index(@PathVariable String gameId) {
        return Collections.singletonMap("messages", "Hello World");
    }
}
```

I like to include the `@PathVariable` annotation to convey the parameter context. However, it is not required. 

It is easy to migrate Spring Boot request mapping annotations to Micronaut annotations. Micronaut includes annotations for the HTTP verbs `@Get`, `@Post`, `@Delete`, `@Put`... They are succinct. Succinct code is good code.  