---
title: AWS CodeBuild Buildspec.yml example for Gradle and a Micronaut Application
summary: I often use CodeBuild to build Micronaut appliations built with Gradle. This post includes an example of a buildspec.yml file.
date_published: 2021-05-24T18:05:34+01:00
keywords:micronaut,aws,codebuild
---

# [%title]

I use [Gradle](https://gradle.org) as my build tool of choice for my [Micronaut](https://micronaut.io) applications. 

I often use [AWS CodeBuild](https://aws.amazon.com/codebuild/) as my continuous integration service. 

I use such a [`buildspec.yml` file](https://docs.aws.amazon.com/codebuild/latest/userguide/build-spec-ref.html): 

> A buildspec is a collection of build commands and related settings, in YAML format, that CodeBuild uses to run a build. 

```yaml
version: 0.2

phases:
  install:
    runtime-versions:
      java: corretto11 
  build:
    commands:
      - ./gradlew build --scan
  post_build:
    finally:
      - rm -f  /root/.gradle/caches/modules-2/modules-2.lock
      - rm -fr /root/.gradle/caches/*/plugin-resolution/
artifacts:
  files:
    - '*-all.jar'
  base-directory: 'build/libs'
cache:
  paths:
    - '/root/.gradle/caches/**/*' 
    - '/root/.gradle/wrapper/**/*'

```

My builds include the [Gradle Scan plugin](https://scans.gradle.com).

Because of that I can include the argument `--scan` which publishes a build scan. An available build scan makes easy to investigate build failures (access tests reports etc). 