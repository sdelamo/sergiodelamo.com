---
title: SpringBoot to Micronaut - @ResponseStatus Exception
summary: In SpringBoot you can annotate your exceptions with @ResponseStatus, Micronaut's HttpStatusException achieves a similar behaviour
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-04-28T08:05:01+01:00
date_modified: 2021-04-28T08:05:01+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #micronaut #springboot

In SpringBoot you can annotate your exceptions with `@ResponseStatus`, Micronaut's `HttpStatusException` achieves a similar behaviour

## [SpringBoot](https://spring.io/projects/spring-boot)

In SpringBoot you can annotate an exception with `@ResponseStatus`

```java
package scorekeep;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Game does not exist.")
public class GameNotFoundException extends RuntimeException {
}
```

If your logic raises such a exception:


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
		if (!gameId.equals("AAA")) {
			throw new GameNotFoundException();
		}
		return Collections.singletonMap("messages", "Hello World");
	}
}
```

You get:

```bash
% curl -i localhost:8080/api/score/ZZZ
HTTP/1.1 422 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 27 Apr 2021 06:09:45 GMT

{"timestamp":"2021-04-27T06:09:45.703+00:00","status":422,"error":"Unprocessable Entity","message":"","path":"/api/score/ZZZ"}
```


## [Micronaut](https:/micronaut.io)

A similar error processing in Micronaut could be achieve by extending `HttpStatusException`

```java
package scorekeep;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;

public class GameNotFoundException extends HttpStatusException {
	public GameNotFoundException() {
		super(HttpStatus.UNPROCESSABLE_ENTITY, "Game does not exist.");
	}
}
```

If your logic raises such a exception:

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
		if (!gameId.equals("AAA")) {
			throw new GameNotFoundException();
		}
		return Collections.singletonMap("messages", "Hello World");
	}
}
```

You get:

```bash
curl -i localhost:8080/api/score/ZZZ
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json
content-length: 96
connection: keep-alive

{"message":"Game does not exist.","_links":{"self":{"href":"/api/score/ZZZ","templated":false}}}%  
```

### Error Formatting

By default, Micronaut error formatting uses [vnd.error](https://github.com/blongden/vnd.error). I prefer to use [Problem+JSON](https://tools.ietf.org/html/rfc7807). To use Problem+JSON in Micronaut, include [micronaut-problem-json](https://micronaut-projects.github.io/micronaut-problem-json/snapshot/guide/) dependency and you will get:

```bash
curl -i localhost:8080/api/score/ZZZ
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/problem+json
content-length: 67
connection: keep-alive
{"type":"about:blank","status":422,"detail":"Game does not exist."}
```

Moreover, [Micronaut Error Handling](https://docs.micronaut.io/latest/guide/#exceptionHandler) is easy to customise to suit your needs.
