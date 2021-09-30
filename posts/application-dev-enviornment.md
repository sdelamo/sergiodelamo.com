---
title: Micronaut development environment
summary: I use Micronaut default enviornment to define a development environment.
date_published: 2021-09-30T09:45:38+01:00
keywords:micronaut,environment
banner_image: https://images.sergiodelamo.com/micronaut-development-environment.png
---

# [%title]

I use [Micronaut Default Environment](https://docs.micronaut.io/latest/guide/#environments) to define a development environment. 

I replace the `Application` class: 
    
```java
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
   }
}
``` 

with: 

```java
public class Application {
    public static void main(String[] args) {
           Micronaut.build(args)
                .mainClass(Application.class)
                .defaultEnvironments(Environment.DEVELOPMENT)
                .start();
    }
}
```

Then, I add to `.gitignore` a configuration file for the `dev` environment. 

```
...
..
.
src/main/resources/application-dev.yml
```

What do I use `application-dev.yml` for? I have configuration for my local database, [disable cookie access restriction](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies#restrict_access_to_cookies) while running in `localhost`, etc. Because of the git ignore inclusion, each developer in the team can have an `application-dev.yml` tailored to his machine.