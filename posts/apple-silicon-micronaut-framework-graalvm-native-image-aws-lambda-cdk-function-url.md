---
title: How to build a GraalVM Native executable of a Micronaut application from an Apple Silicon Mac and deploy it to AWS Lambda ARM architecture.
summary: This post highlights the necessary changes to the application build to deploy a GraalVM Native executable to AWS Lambda Custom Runtime with an arm64 architecture.
date_published: 2022-09-29T10:34:33+01:00
keywords:micronaut,aws,lambda,cdk,graalvm,apple-silicon
---

# [%title]

[%summary]


## Lambda instruction set architectures

[AWS Lambda supports multiple architectures](https://docs.aws.amazon.com/lambda/latest/dg/foundation-arch.html):

> The instruction set architecture of a Lambda function determines the type of computer processor that Lambda uses to run the function. Lambda provides a choice of instruction set architectures:

> `arm64` – 64-bit ARM architecture, for the AWS Graviton2 processor. The arm64 architecture is available in most AWS Regions.
> `x86_64` – 64-bit x86 architecture, for x86-based processors.

## Java Version for macOS ARM 64. 

Most modern [Mac computers run Apple silicon](https://support.apple.com/en-us/HT211814). You need a Java Version for macOS ARM 64. 

I use the highest Corretto version supported in the AWS [Lambda Runtimes](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html) when working with Lambda. It is easy to select if you use [SDKMAN](https://sdkman.io).

```
================================================================================
Available Java Versions for macOS ARM 64bit
================================================================================
 Vendor        | Use | Version      | Dist    | Status     | Identifier
--------------------------------------------------------------------------------
 Corretto      |     | 19           | amzn    |            | 19-amzn             
               |     | 17.0.4       | amzn    |            | 17.0.4-amzn         
               | >>> | 11.0.16      | amzn    | installed  | 11.0.16-amzn        
               |     | 8.0.342      | amzn    |            | 8.0.342-amzn 
```

## Generate the Micronaut Application for AWS Lambda

The easiest way to generate Micronaut Applications is to use Micronaut CLI or [Micronaut Launch](https://launch.micronaut.io/).

Generate a [`Function Application for Serverless` with the following features: `graalvm`, `aws-cdk`, `aws-lambda`,  and `aws-lambda-function-url`](https://micronaut.io/launch?type=FUNCTION&features=graalvm&features=aws-cdk&features=aws-lambda-function-url&features=aws-lambda). 


## Selecting ARM Architecture

If you use Cloud Formation or [AWS Cloud Development Kit](https://aws.amazon.com/cdk/), to create your infrastructure, select ARM as the architecture.

```java
...
..
.
  .logRetention(RetentionDays.ONE_WEEK)
  .architecture(Architecture.ARM_64)
  .build();
```  

## GraalVM native image generation for Lambda with Gradle

[Micronaut Gradle Plugin](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#_deploying_to_aws_lambda_as_graalvm_native_image) provides a Gradle task `buildNativeLambda` to help with the GraalVM Native executable generation.

> If you are interested in deploying your Micronaut application to AWS Lambda using GraalVM you only need to set the runtime to lambda and execute ./gradlew buildNativeLambda. This task will generate a GraalVM native image inside a Docker container and then it will create the file build/libs/your-app.zip file ready to be deployed to AWS Lambda using a custom runtime. 

## Controlling the target architecture of Lambda GraalVM native image 

[Micronaut Gradle Plugin](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#_deploying_to_aws_lambda_as_graalvm_native_image) offers automatic detection:

> The plugin will detect the host operating system architecture (based on the `os.arch` Java system property) and will install the corresponding GraalVM binary distribution inside the Docker image. This means that when running packaging from an X86_64 (Intel/AMD) machine, the produced native image will be an amd64 binary, whilst on an ARM host (such as the new Mac M1) it will be an aarch64 binary.

Moreover, you can control it manually:

```
dockerfileNative {
    graalArch.set(org.apache.tools.ant.taskdefs.condition.Os.isArch("aarch64") ? "aarch64" : "amd64")
}
```

## GraalVM native image generation for Lambda with Maven

You can achieve the native images for Lambda with the [Micronaut Maven Plugin](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/examples/package.html#building_jvm_based_docker_images):

 
```
mvn package -Dpackaging=docker -Dmicronaut.runtime=lambda
```

## Force latest version of docker-java

When working with Apple Silicon and ARM architecture you need force the Micronaut Gradle plugin to use the latest version of [Docker-java](https://github.com/docker-java/docker-java#docker-java). 

```
buildscript {
    dependencies {
        classpath("com.github.docker-java:docker-java-transport-httpclient5:3.2.13") {
            because("M1 macs need a later version of JNA")
        }
    }
}
```

## Mixing Architectures

It is impossible to generate a GraalVM native Image of your Micronaut Application in an Apple Silicon Mac and deploy it to an AWS Lambda Custom Runtime using x86 architecture.

## Deployment instructions

The `README.md` file of the [generated application](https://micronaut.io/launch?type=FUNCTION&features=graalvm&features=aws-cdk&features=aws-lambda-function-url&features=aws-lambda) contains deployment instructions. 
