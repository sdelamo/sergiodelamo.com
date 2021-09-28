---
title: Micronaut Health Check
summary: The health endpoint is the easiest way to expose a health check in your Micronaut application.
date_published: 2021-09-28T10:17:41+01:00
keywords:micronaut
banner_image: https://images.sergiodelamo.com/micronaut-health-check.png
---

# [%title]

The easiest way to expose a [health check](https://aws.amazon.com/builders-library/implementing-health-checks/) in your Micronaut Application is to enable the [health endpoint](https://docs.micronaut.io/latest/guide/#healthEndpoint).

Just by adding the dependency `io.micronaut:micronaut-management` your application exposes a GET `/health` route which you can use as a health check. 
Add a [test which verifies the application exposes the health endpoint](https://sergiodelamo.com/blog/micronaut-test-health-endpoint.html).