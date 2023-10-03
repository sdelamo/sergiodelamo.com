---
title: Gradle Java Test Fixtures Plugin
summary: Gradle Plugin which automatically create a `testFixtures` source set, in which you can write your test fixtures.
date_published: 2023-10-03T12:05:33+01:00
keywords:gradle,test
external_url: https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures
---

# [%title]

[%summary]

> Test fixtures are commonly used to setup the code under test, or provide utilities aimed at facilitating the tests of a component. Java projects can enable test fixtures support by applying the `java-test-fixtures` plugin, in addition to the java or java-library plugins.

> This will automatically create a testFixtures source set, in which you can write your test fixtures. Test fixtures are configured so that:

> - they can see the main source set classes

> test sources can see the test fixtures classes