---
title: Micronaut® Framework and AWS Lambda
summary: This talk introduces the Micronaut framework integration with AWS Lambda.
date_published: 2022-01-28T17:28:31+01:00
keywords:presentation,micronaut,aws,lambda,graalvm
---

# [%title]

Similar presentation at [Barcelona JUG](https://www.youtube.com/watch?v=S3qYOVNaKS8&t=195s), and [Micronaut Framework and AWS Webinar](https://sergiodelamo.com/blog/combining-micronaut-framework-and-aws.html).

---

In this session, [Sergio del Amo](https://sergiodelamo.com) introduces the [Micronaut framework](https://micronaut.io) integration with [AWS Lambda](https://aws.amazon.com/lambda/). 

Can my Lambda functions be fast if I use Java? Can I write my applications with everything I love (dependency injection, configuration injection, ease routing) and deploy to AWS Lambda? In this deep dive, you will that it is possible with the Micronaut Framework.  

You will learn how to fast cold startups with:

- The GraalVM native executable generation built-in in the Micronaut Gradle and Maven plugins and the Micronaut AWS integration with AWS Lambda Custom runtimes.  

- 
- Micronaut integration with Amazon Cloud Development Kit (CDK) to generate infrastructure as code. You can setup your Amazon API Gateway, AWS Lambda function and DynamoDB table without effort.  

Micronaut Framework supports deploying functions to the AWS Lambda Java runtime or as native executables built with GraalVM to a custom runtime. 
Moreover, with [Micronaut CRaC](https://micronaut-projects.github.io/micronaut-crac/latest/guide/) it integrates seamlessly with AWS Lambda Snapstart. With Micronaut framework, you can get fast cold startups for your Java applications.

Moreover, the framework offer

Java applications have struggled in AWS Lambda. Mainly due to slow cold starts.

Micronaut applications' characteristics, such as fast start-up, low memory consumption, and [GraalVM](https://www.graalvm.org) integration, can help you workaround cold startups in AWS Lambda.

You will learn the following: 

- How AWS Lambda functions written with the Micronaut framework can be triggered with AWS Events (such as an S3 or DynamoDB event) or Alexa Skills events
- How integrating Amazon API Gateway and AWS Lambda enables you to write your applications as you would with a Netty runtime (i.e., write your applications as you usually do; run them in AWS Lambda)
- How to build AWS Lambda Functions with the Micronaut framework and deploy them to a Java runtime, use SnapStart and CRaC, or deploy to a custom runtime as a GraalVM Native image. 

Sergio del Amo is a Micronaut core committer,  Developer Advocate for the Micronaut Foundation, and host of the [Micronaut podcast](https://micronautpodcast.com).

## Elevator Pitch 

Java applications have struggled in AWS Lambda. Mainly due to slow cold starts. Building your functions with the Micronaut Framework makes AWS Lambda a viable platform. 