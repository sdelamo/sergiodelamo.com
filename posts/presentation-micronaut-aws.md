---
title: Micronaut Framework and AWS Lambda
summary: This talk explains how to deploy Micronaut applications to AWS Lambda.
date_published: 2022-01-28T17:28:31+01:00
keywords:presentation,micronaut
---

# [%title]

Presented at [an online Micronaut Webinar](https://sergiodelamo.com/blog/combining-micronaut-framework-and-aws.html),

In this session, [Sergio del Amo](https://sergiodelamo.com) introduces the Micronaut framework integration with AWS Lambda. Sergio is a Micronaut core committer and Micronaut developer advocate.

You will learn how to build AWS Lambda Functions with the Micronaut framework and how to deploy them to a Java runtime or a custom runtime as a GraalVM Native image. 

Micronaut applications' characteristics such as fast start-up, low memory consumption, and GraalVM integration help you workaround cold-startups in AWS Lambda.

AWS Lambda functions written with the Micronaut Framework can be triggered with AWS Events (such as an S3 or DynamoDB event) or Alexa Skills events. 

Moreover, if you integrate Amazon API Gateway and AWS Lambda you can write your Micronaut as you would do with a Netty runtime. Write your applications as you are used to. Run them in AWS Lambda. 