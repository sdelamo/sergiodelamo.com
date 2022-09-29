---
title: GraalVM Native executable fo a Micronaut application with AWS CDK, AWS Lambda (Custom Runtime), and function URL.
summary: Generate and deploy a Micronaut Application as a GraalVM Native executable with a multi-project build with CDK and a Lambda function associated with an HTTP(s) endpoint via a function URL.
date_published: 2022-09-29T10:34:33+01:00
keywords:micronaut,aws,lambda,cdk,graalvm
---

# [%title]

[%summary]

## Generate the application

The easiest way to generate Micronaut Applications is to use Micronaut CLI or [Micronaut Launch](https://launch.micronaut.io/).

Generate a [`Function Application for Serverless` with the following features: `graalvm`, `aws-cdk`, `aws-lambda`,  and `aws-lambda-function-url`](https://micronaut.io/launch?type=FUNCTION&features=graalvm&features=aws-cdk&features=aws-lambda-function-url&features=aws-lambda). 

The generated project is a multi-project build with a structure similar to the following listing (if you selected Gradle as your build tool):

```
.
├── README.md
├── app
│   ├── build.gradle
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── FunctionRequestHandler.java
│       │   └── resources
│       │       ├── application.yml
│       │       └── logback.xml
│       └── test
│           └── java
│               └── com
│                   └── example
│                       └── FunctionRequestHandlerTest.java
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── infra
│   ├── build.gradle
│   ├── cdk.json
│   └── src
│       ├── main
│       │   └── java
│       │       └── com
│       │           └── example
│       │               ├── AppStack.java
│       │               └── Main.java
│       └── test
│           └── java
│               └── com
│                   └── example
│                       └── AppStackTest.java
├── micronaut-cli.yml
├── settings.gradle
└── test-lambda.sh
```

There are two folders: 

- `app`  - where your application code lives. 
- `infra` - where the code describing the resources being created resides. 

## Infrastructure as Code

To generate the infrastructure, we leverage [AWS Cloud Development Kit](https://aws.amazon.com/cdk/).

> The AWS Cloud Development Kit (AWS CDK) is an open-source software development framework to define your cloud application resources using familiar programming languages.

The infrastructure is created via:

`infra/src/main/java/com/example/AppStack.java`

It creates an AWS Lambda Function with Custom Runtime, with one-week log retention, 512 MB of memory, timeout of 10s, and active tracing. 

## Deployment instructions

The `README.md` file contains deployment instructions. You need to [install AWS CDK CLI](install-aws-cdk.html) to deploy.

```
./gradlew :app:buildNativeLambda
./gradlew test
cd infra
cdk synth
cdk deploy
```

## Outputs

After deployment, the console output shows something similar to: 

```
...
Outputs:
MicronautAppStack.MnTestApiUrl = https://5xh3ybw2k6gna4bwc5ezjxyywq0eyutp.lambda-url.us-east-1.on.aws/
```

Hit the endpoint, and you get a 200 Hello World JSON response:

```
HTTP/1.1 200 OK
Date: Thu, 29 Sep 2022 09:57:28 GMT
Content-Type: application/json
Content-Length: 25
Connection: keep-alive
x-amzn-RequestId: ee116b51-596f-48a7-9435-7a0ec5927d65
X-Amzn-Trace-Id: root=1-63356c07-6630a41c766c6847644902f6;sampled=1

{"message":"Hello World"}
```

## Handler

The Lambda Handler is at:


`app/src/main/java/com/example/FunctionRequestHandler.java`

It uses [AWS Lambda Java Events](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events) to receive and respond to the HTTP Trigger.

## Cold Start Performance

You can visit the AWS Console and go to the `Monitor` tab. In the `Traces` section, you can find information about the performance of your function for cold starts.

```java
Initialization: 382ms
Invocation: 31ms
```


## Cleanup

The `README.md` contains instructions to clean up the resources you created. 

```
cd infra
cdk destroy
```


