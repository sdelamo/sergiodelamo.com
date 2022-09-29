---
title: Micronaut Framework AWS CDK, AWS Lambda (Java Runtime), and function URLs
summary: Generate a Micronaut Application with a multi-project build with CDK and a Lambda function associated with an HTTP(s) endpoint via a function URL.
date_published: 2022-09-29T10:34:33+01:00
keywords:micronaut,aws,lambda,cdk
---

# [%title]

[%summary]

## Generate the application

The easiest way to generate Micronaut Applications is to use Micronaut CLI or [Micronaut Launch](https://launch.micronaut.io/).

Generate a [`Function Application for Serverless` with the following features `aws-cdk`, `aws-lambda`,  and `aws-lambda-function-url`](https://micronaut.io/launch?type=FUNCTION&features=aws-cdk&features=aws-lambda-function-url&features=aws-lambda). 

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

It creates an AWS Lambda Function with Java 11 Runtime, with one-week log retention, 512 MB of memory, timeout of 10s, and active tracing. 

## Deployment instructions

The `README.md` file contains deployment instructions. You need to [install AWS CDK CLI](install-aws-cdk.html) to deploy.

```
./gradlew shadowJar
./gradlew test
cd infra
cdk synth
cdk deploy
```

If you have never run CDK commands in the AWS account, you must first run `cdk bootstrap`. 

## Outputs

After deployment, the console output shows something similar to: 

```
...
Outputs:
MicronautAppStack.MnTestApiUrl = https://6mvj6q5jjore5kxyh2uh52k5fa0yyuwq.lambda-url.us-east-1.on.aws/
```

Hit the endpoint, and you get a 200 Hello World JSON response:

```
curl -i https://6mvj6q5jjore5kxyh2uh52k5fa0yyuwq.lambda-url.us-east-1.on.aws/
HTTP/1.1 200 OK
Date: Thu, 29 Sep 2022 09:17:39 GMT
Content-Type: application/json
Content-Length: 25
Connection: keep-alive
x-amzn-RequestId: 8ce868d1-b6a5-4aca-9801-90e7f9c1ac48
X-Amzn-Trace-Id: root=1-633562b0-70c6fad813e10b370d55ba83;sampled=1

{"message":"Hello World"}%        
```

## Handler

The Lambda Handler is at:


`app/src/main/java/com/example/FunctionRequestHandler.java`

It uses [AWS Lambda Java Events](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events) to receive and respond to the HTTP Trigger.

## Cold Start Performance

You can visit the AWS Console and go to the `Monitor` tab. In the `Traces` section, you can find information about the performance of your function for cold starts.

```java
Initialization: 2.17s
Invocation: 190ms
```



## Cleanup

The `README.md` contains instructions to clean up the resources you created. 

```
cd infra
cdk destroy
```


