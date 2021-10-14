---
title: cURL command to get the exact number of stars in a Github Project
summary: I am trying to collect some metrics for Micronaut Advocacy
date_published: 2021-10-14T10:37:25+01:00
keywords:github,curl
---

# [%title]

I am trying to collect some metrics to measure my [advocacy job](https://sergiodelamo.com/blog/2gm-developer-advocate.html).

The following [cURL](https://curl.se/) command returns the number of [Github Stars](https://docs.github.com/en/get-started/exploring-projects-on-github/saving-repositories-with-stars#about-stars) for [micronaut-core repository](https://github.com/micronaut-projects/micronaut-core).

```bash
curl https://api.github.com/repos/micronaut-projects/micronaut-core | grep 'stargazers_count' 
```
