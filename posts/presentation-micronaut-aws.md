---
title: Micronaut® Framework and AWS Lambda
summary: This talk introduces the Micronaut framework integration with AWS Lambda.
date_published: 2022-01-28T17:28:31+01:00
keywords:presentation,micronaut,aws,lambda,graalvm
---

# [%title]

Similar presentation at [Barcelona JUG](https://www.youtube.com/watch?v=S3qYOVNaKS8&t=195s), [Micronaut Framework and AWS Webinar](https://sergiodelamo.com/blog/combining-micronaut-framework-and-aws.html), and [Micronaut AWS Lambda SnapStart integration](https://www.youtube.com/watch?v=PBvlwT27lKA).


---

Can a Lambda function be fast if I use Java? Can I write my applications with a framework and still deploy them to AWS Lambda? Yes. It is possible with the Micronaut Framework. 

Micronaut applications' characteristics, such as fast startup and low memory consumption, are perfect for Lambda, but the framework goes the extra mile to help you get fast cold startups with:

- GraalVM native image generation built-in in the Micronaut Gradle and Maven plugins and the integration with AWS Lambda custom runtimes.
- Micronaut CRaC integration with AWS Lambda Snapstart. You can get amazingly fast cold startups while using Lambda's Java runtime. 

Additionally, you learn: 

- How integrating Amazon API Gateway and AWS Lambda enables you to write your applications as you would with a Netty runtime (`@Controller's...) and run them in AWS Lambda.
- Micronaut integration with Amazon Cloud Development Kit (CDK) to generate infrastructure as code. Set up easily a Serverless Architecture with Amazon API Gateway, AWS Lambda function, and DynamoDB table.
- How to use reflection-free serialization in Lambda with Micronaut Serialisation. 
- How to integrate Amazon Cognito, Lambda, and Micronaut Security. 
- Different function styles depending on your Lambda Trigger.
- How to develop and test your Lambda Functions.

Attendees will gain an understanding of the vast Micronaut integration with AWS Lambda. They will be ready to start using Java with Lambda. 

## Elevator Pitch 

Can a Lambda function be fast if I use Java? Can I write my applications with a framework and still deploy them to AWS Lambda? Yes. It is possible with the Micronaut Framework and its Lambda, GraalVM, and Snapstart integration. 

