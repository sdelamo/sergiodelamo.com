---
title: Micronaut Framework and AWS Lambda
summary: This talk explains how to deploy Micronaut applications to AWS Lambda.
date_published: 2022-01-28T17:28:31+01:00
keywords:presentation,micronaut,aws,lambda,graalvm
---

# [%title]

Similar presentation at [an online Micronaut Webinar](https://sergiodelamo.com/blog/combining-micronaut-framework-and-aws.html),

In this session, [Sergio del Amo](https://sergiodelamo.com) introduces the [Micronaut framework](https://micronaut.io) integration with [AWS Lambda](https://aws.amazon.com/lambda/). 

Java applications have struggled in AWS Lambda. Mostly, due to slow cold starts.

Micronaut applications' characteristics such as fast start-up, low memory consumption, and [GraalVM](https://www.graalvm.org) integration can help you workaround cold-startups in AWS Lambda.

AWS Lambda functions written with the Micronaut Framework can be triggered with AWS Events (such as an S3 or DynamoDB event) or Alexa Skills events. 

Moreover, if you integrate Amazon API Gateway and AWS Lambda you can write your Micronaut as you would do with a Netty runtime. Write your applications as you are used to. Run them in AWS Lambda. 

You will learn how to build AWS Lambda Functions with the Micronaut framework and how to deploy them to a Java runtime or a custom runtime as a GraalVM Native image. Java applications can be a viable option for AWS Lambda. 

Sergio del Amo is a Micronaut core committer,  developer advocate, and host of the [Micronaut podcast](https://micronautpodcast.com).