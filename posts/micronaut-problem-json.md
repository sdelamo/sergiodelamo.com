---
title: Micronaut Error Responses with Problem+JSON
summary: You can use problem+json instead of vnd.error just by adding Micronaut Problem dependency.
date_published: 2021-09-28T12:07:35+01:00
keywords:micronaut,problem,vnd
banner_image: https://images.sergiodelamo.com/micronaut-error-responses-with-problem+json.png
---

# [%title]

[problem+json](https://datatracker.ietf.org/doc/html/rfc7807) is my preferred way of formatting API errors and it is easy to use it in a Micronaut Application. 

A request to a non existing route:

```bash
curl -i locahost:8080/api/v1/bogus
```

returns:

```json
{
    "message":"Not Found",
    "_links":{"self":{"href":"/api/v1/bogus","templated":false}},
    "_embedded":{"errors":[{"message":"Page Not Found"}]}
}
```

The previous payload is formatted with [vnd.error](https://github.com/blongden/vnd.error). The response content type is `application/json`.

If you add the dependency `io.micronaut.problem:micronaut-problem-json`, the maven coordinate of [Micronaut Problem JSON](https://micronaut-projects.github.io/micronaut-problem-json/latest/guide/), the same request: 

```bash
curl -i locahost:8080/api/v1/bogus
```

returns:

```json
{
    "type":"about:blank",
    "status":404,
    "detail":"Page Not Found"
}
```

Moreover, the response content type is `application/problem+json`. 

[Micronaut Problem JSON](https://micronaut-projects.github.io/micronaut-problem-json/latest/guide/) helps you produce application/problem+json responses from a Micronaut application. It connects the [Problem library](https://github.com/zalando/problem) and [Micronaut Error](https://docs.micronaut.io/latest/guide/#errorFormatting) Formatting capabilities.





