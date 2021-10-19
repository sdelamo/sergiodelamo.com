---
title: SpringBoot to Micronaut framework - Application Class
summary: SpringBoot and Micronaut applications contain a simple application class which starts the application for you.
date_published: 2021-04-27T06:45:56+01:00
keywords:micronaut,springboot
---

# [%title]

[%summary]

[SpringBoot](https://spring.io/projects/spring-boot):

```java
package scorekeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

[Micronaut framework](https:/micronaut.io):


```java
package scorekeep;

import io.micronaut.runtime.Micronaut;

public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}
}
```

Except for the `@SpringBootApplication` annotation both classes are almost identical. 