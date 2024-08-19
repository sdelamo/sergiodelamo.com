---
title: Disable Testcontainers JUnit 5 tests without Docker
summary: [Testcontainers Junit5](https://java.testcontainers.org/test_framework_integration/junit_5/) integration allows you to toggle these tests by annotating them with `@Testcontainers(disabledWithoutDocker = true)`
date_published: 2024-08-19T09:17:50+01:00
keywords:docker,testcontainers,junit
---

# [%title]

[Testcontainers](https://testcontainers.com) simplifies testing against third-party components such as databases. But sometimes, you want to run your Junit 5 tests and skip those requiring Docker. 

> To run Testcontainers-based tests, you need a Docker-API compatible container runtime, such as using Testcontainers Cloud or installing Docker locally.

Luckily, [Testcontainers Junit5](https://java.testcontainers.org/test_framework_integration/junit_5/) integration allows you to toggle these tests by annotating them with `@Testcontainers(disabledWithoutDocker = true)`. 

See an [example of a commit in Micronaut Test](https://github.com/micronaut-projects/micronaut-test/pull/1081/commits/47920a39869d0b8978ff58a792f5a7d70f12baa5).

