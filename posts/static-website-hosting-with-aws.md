---
title: Static Website Hosting with AWS
summary: CloudFront, S3, AWS Certificate Manager and Route53
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-06T07:53:06+01:00
date_modified: 2021-08-06T07:53:06+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #aws #s3 #cloudfront #route53 #certificate-manager

I host this website on [AWS](https://aws.amazon.com). It is a static website which I generate with a [Gradle](https://gradle.org) build.

This is the architecture diagram:

![](https://images.sergiodelamo.com/static-website-aws-architecture-s3-cloudfront-certificate-manager-route53.png)

## S3

I have two [Amazon S3](https://aws.amazon.com/s3/) buckets. One for the code (HTML, CSS...) and another bucket for the media (Images, Videos...)

## AWS Certificate Manager 

I created a certificate for the domain name with 
[AWS Certificate Manager](https://aws.amazon.com/certificate-manager/). The certificate covers the domain apex `sergiodelamo.com` and the wildcard `*.sergiodelamo.com`

## CloudFront 

I have two [Amazon CloudFront](https://aws.amazon.com/cloudfront/) distributions distributing the content of each bucket. One distribution for the code bucket `sergiodelamo.com` and another one for the media `images.sergiodelamo.com`. Both distributions use the certificate. One distribution uses the domain `sergiodelamo.com`, the other the domain `images.sergiodelamo.com`.

## Route53

I manage the website domain name at [Hover](https://www.hover.com). I delegate DNS to [Amazon Route53](https://aws.amazon.com/route53/). In Route53, I have two DNS Records A which are alias to the CloudFront distributions. 

Such an architecture is simpler than it looks like to create. I am really happy with it. 