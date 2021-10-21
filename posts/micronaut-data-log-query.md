---
title: Log Micronaut® Data queries
banner_image: https://images.sergiodelamo.com/log-micronaut-data-queries.png
summary: I often add this logger to log my queries when using Micronaut Data.
date_published: 2021-10-21T07:26:31+01:00
keywords:micronaut,micronautdata
---

# [%title]

[%summary]

_src/main/resources/logback.xml_
```xml
    <logger name="io.micronaut.data.query" level="TRACE"/>
</configuration>
```

Then, you will get an output such as: 

```bash
Executing SQL Insert: INSERT INTO `contact` (`name`,`job_title`,`id`) VALUES (?,?,?)
Binding parameter at position 1 to value Tim Cook with data type: STRING
Binding parameter at position 2 to value CEO with data type: STRING
Binding parameter at position 3 to value Df6saLdgrlItF6IB9C6tU2r9Bvg with data type: STRING
```

It is important to get visibility about the queries your application is executing. 


