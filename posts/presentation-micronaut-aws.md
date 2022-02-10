---
title: Micronaut® Framework and AWS Lambda
summary: This talk introduces the Micronaut framework integration with AWS Lambda.
date_published: 2022-01-28T17:28:31+01:00
keywords:presentation,micronaut,aws,lambda,graalvm
---

# [%title]

Similar presentation at [an online Micronaut Webinar](https://sergiodelamo.com/blog/combining-micronaut-framework-and-aws.html),

---

In this session, [Sergio del Amo](https://sergiodelamo.com) introduces the [Micronaut framework](https://micronaut.io) integration with [AWS Lambda](https://aws.amazon.com/lambda/). 

Java applications have struggled in AWS Lambda. Mainly due to slow cold starts.

Micronaut applications' characteristics such as fast start-up, low memory consumption, and [GraalVM](https://www.graalvm.org) integration can help you workaround cold-startups in AWS Lambda.

You will learn: 

- How AWS Lambda functions written with the Micronaut framework can be triggered with AWS Events (such as an S3 or DynamoDB event) or Alexa Skills events
- How integrating Amazon API Gateway and AWS Lambda enables you to write your applications as you would with a Netty runtime (i.e., write your applications as you usually do; run them in AWS Lambda)
- How to build AWS Lambda Functions with the Micronaut framework and deploy them to a Java runtime or a custom runtime as a GraalVM Native image. 

Sergio del Amo is a Micronaut core committer,  Developer Advocate for the Micronaut Foundation, and host of the [Micronaut podcast](https://micronautpodcast.com).