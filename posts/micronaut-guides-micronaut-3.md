---
title: Micronaut Guides updated to 3!
banner_image: https://images.sergiodelamo.com/micronaut-guides-updated-to-3.png
external_url: https://micronaut.io/guides/
summary: Guide are ready for Micronaut Framework 3!
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-18T22:40:29+01:00
date_modified: 2021-08-18T22:40:29+01:00
keywords:micronaut
---

# [%title]

[Micronaut Guides]([%external_url]) are step by step tutorials to help you learn the [Micronaut Framework](https://micronaut.io). 

My colleague [Iván López](https://twitter.com/ilopmar) wrote about the [improvements to Micronaut Guides infrastructure](https://micronaut.io/2021/04/12/improving-the-micronaut-guides-infrastructure/) back in April. We have continued to improve and automate the process. 

Thanks to this focus towards automation, [Micronaut Guides](https://guides.micronaut.io) are updated to the latest version of the framework. [Micronaut Framework 3.0.0 was released today](https://micronaut.io/2021/08/18/micronaut-framework-3-released/). There are 53 guides. Many of them with up to 6 combinations (3 languages and 2 build tools). Every guide contains a ZIP file with the sample of code written with Micronaut Framework 3.0.0. Every guide contains code samples. Every code sample uses 3.0.0.

## Guides versions

We version Micronaut Guides. You can find the latest (using Micronaut framework 3.0.0) at: 

[[%external_url]]([%external_url])

But if you want to check guides for 2.5.x, you can find them at: 

[https://guides.micronaut.io/2.5.x/index.html](https://guides.micronaut.io/2.5.x/index.html)

## From 2.5.x to 3.0.x

The [pull request to update Micronaut Guides to 3.0.0](https://github.com/micronaut-projects/micronaut-guides/pull/388) is a good representation of the changes necessary to update projects from Micronaut Framework 2.5.x to 3.0.x.

- Replacements of `import javax.inject` with `import jakarta.inject`. 
- Migration from [RxJava2](https://github.com/ReactiveX/RxJava/tree/2.x) to [Project Reactor](https://projectreactor.io). 
- Changes in the security guides. Micronaut Security 3 removes `UserDetails` in favour of `Authentication`. `Authentication` and `AuthenticationResponse` have new static methods to build instances. 
- Small changes (package renaming, use of `BeanProvider`, ...)


