---
title: Micronaut 3 Released
banner_image: https://images.sergiodelamo.com/micronaut-3-released.png
external_url: https://micronaut.io/2021/08/18/micronaut-framework-3-released/
summary: Update your apps to the latest version of Micronaut
date_published: 2021-08-18T22:08:51+01:00
keywords:micronaut
---

# [%title]

> A major release of the Framework has given us the opportunity to fix the design mistakes of the past and implement important changes to make the Framework more intuitive to use and adaptable to future requirements.

The update should not be difficult. You will need to update to the `jakarta.inject` annotations and to the Micronaut nullability annotations. If you were RxJava2 code, we recommend you to update to Project Reactor but you can keep using RxJava 2 simply by adding a dependency.  

There are improvements in every module but the DI and AOP improvements in core are really powerful:

- Injection by generics. 
- Qualifier annotations
- Limit injectable types
- AOP Interceptions for constructors and life cycle methods(`@PostConstruct`, ...)


