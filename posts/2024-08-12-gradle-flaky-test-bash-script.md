---
title:  Bash script to run a Gradle flaky test a hundred times
summary:  Today, I was fighting a flaky test in a Micronaut project. I used this simple bash script to run the test a hundred times.
date_published: 2024-08-12T13:08:09+01:00
keywords:bash,gradle
---

# [%title]

[%summary]

```
#!/bin/bash

EXIT_STATUS=0

for ((i=1; i<=100; i++))
do
  ./gradlew :http-client:test --tests "io.micronaut.http.client.aop.NotFoundSpec.test 404 handling with Flowable" --rerun-tasks || EXIT_STATUS=$?
  if [ $EXIT_STATUS -ne 0 ]; then
    exit $EXIT_STATUS
  fi
done

exit $EXIT_STATUS
```