---
title: CodePipeline for a Micronaut application Docker image
summary: Continuous deployment - CodePipeline: Github → CodeBuild → ECR → Elastic Beanstalk  
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-05-25T11:57:27+01:00
date_modified: 2021-05-25T11:57:27+01:00
event.name: What's new in Grails 4
event.start: 2020-12-01 19:00
event.end: 2020-12-01 20:00
event.summary: Grails at the London Groovy User Group
event.location: Online
---

# [%title]


By [%author.name] - [%date_published]

Tags: #micronaut #aws #ecr #codebuild #elasticbeanstalk #codebuild #codepipeline #docker

[%summary]

![](https://images.sergiodelamo.com/codepipeline-github-codebuild-ecr-elasticbeanstalk.svg)

Micronaut is build agnostic, you can use either Gradle or Maven. However, Gradle is my build tool of choice. 

## Changes to Gradle Build

### Generate Dockerrun.aws.json

Create a template for `Dockerrun.aws.json`

> A `Dockerrun.aws.json` file describes how to deploy a remote Docker image as an Elastic Beanstalk application. This JSON file is specific to Elastic Beanstalk. If your application runs on an image that is available in a hosted repository, you can specify the image in a Dockerrun.

_Dockerrun.aws.json_
```json
{
    "AWSEBDockerrunVersion": "1",
    "Image": {
        "Name": "@awsAccountId@.dkr.ecr.@awsRegion@.amazonaws.com/@awsEcrRepositoryName@:@imageTag@",
        "Update": "true"
    },
    "Ports": [
        {
            "ContainerPort": "5000"
        }
    ]
}    
```

I define the properties in `gradle.properties`

```properties
projectVersion=0.0.5
...
awsAccountId=XXXXXXX
awsRegion=us-east-1
awsEcrRepositoryName=YYYYY
````

Create a Gradle tasks to populate the JSON template:

_build.gradle_
```groovy
import org.apache.tools.ant.filters.ReplaceTokens
task copyAndReplaceTokensFromDockerrun(type: Copy) {
    from('Dockerrun.aws.json') {
        include 'Dockerrun.aws.json'
        filter(ReplaceTokens, tokens: [imageTag: projectVersion,
                                       awsAccountId: awsAccountId,
                                       awsRegion: awsRegion,
                                       awsEcrRepositoryName: awsEcrRepositoryName])
    }
    into 'build/docker'
}
```

### Configure Container registry

Configure the [Amazon ECR](https://aws.amazon.com/ecr/) repository as the container registry:

_build.gradle_
```groovy
version = projectVersion

...
..
.

dockerBuild {
  images = ["${awsAccountId}.dkr.ecr.${awsRegion}.amazonaws.com/${awsEcrRepositoryName}:$project.version"]
}
```

## AWS CodeBuild 

When you create your CodeBuild project, in the environment section, check:

> [x] Enable this flag if you want to build Docker images or want your builds to get elevated privileges`

### BuildSpec

Create [`buildspec.yml`](https://docs.aws.amazon.com/codebuild/latest/userguide/build-spec-ref.html) file:

> A buildspec is a collection of build commands and related settings, in YAML format, that CodeBuild uses to run a build.

```yaml
version: 0.2

phases:
  install:
    runtime-versions:
      java: corretto11 
  pre_build:
    commands:
      - echo Logging in to Amazon ECR...
      - $(aws ecr get-login --no-include-email --region $AWS_DEFAULT_REGION)
  build:
    commands:
      - ./gradlew dockerPush
      - ./gradlew copyAndReplaceTokensFromDockerrun
  post_build:
    finally:
      - rm -f  /root/.gradle/caches/modules-2/modules-2.lock
      - rm -fr /root/.gradle/caches/*/plugin-resolution/
artifacts:
  files:
    - 'Dockerrun.aws.json'
  base-directory: 'build/docker'
cache:
  paths:
    - '/root/.gradle/caches/**/*' 
    - '/root/.gradle/wrapper/**/*'
```

- `dockerPush`, a task provided by the [Micronaut Gradle Plugin](https://github.com/micronaut-projects/micronaut-gradle-plugin), pushes a Docker Image built with the [Docker Gradle Plugin](https://github.com/bmuschko/gradle-docker-plugin) to ECR, the configured container registry

The build's artifact is the output of the Gradle `copyAndReplaceTokensFromDockerrun` task. 

### AWS Code Build IAM Role

Attach a policy to the IAM role used by [AWS CodeBuild](https://aws.amazon.com/codebuild/) to allow the build to publishing a new image to a [Amazon ECR](https://aws.amazon.com/ecr/) repository. For example, the AWS Managed policy `AmazonEC2ContainerRegistryPowerUser`. 

## Elastic Beanstalk 

Create a `Web server environment`. As platform select `Docker running on a 64bit Amazon Linux 2`. 

### AWS Elastic Beanstalk IAM Role

Attach a policy to the EC2 IAM role used by [AWS Elastic Beanstalk](https://aws.amazon.com/elasticbeanstalk/) to allow the environment to retrieve the image from [Amazon ECR](https://aws.amazon.com/ecr/) repository. For example, the AWS Managed policy `AmazonEC2ContainerRegistryReadOnly`.

### Start Micronaut Application in port 5000

Set environment variable `MICRONAUT_SERVER_PORT` with value `5000` in [AWS Elastic Beanstalk](https://aws.amazon.com/elasticbeanstalk/) `Configuration → Software`.

## AWS CodePipeline

[AWS CodePipeline](https://aws.amazon.com/codepipeline/):

- Source: Github (Version 2)
- Build Provider: AWS CodeBuild
- Deploy Provider: AWS Elastic Beanstalk