---
title: Bash Script to run the same test multiple times in a Gradle build
summary: The following code contains a bash script I wrote to fight a flaky test.
date_published: 2023-10-17T17:31:47+01:00
keywords:bash,gradle
---

# [%title]

> A flaky test is a software test that yields both passing and failing results despite zero changes to the code or test


```bash
#!/bin/bash
EXIT_STATUS=0
NUM_RUNS=100

# Initialize a variable to track the number of successful runs
SUCCESSFUL_RUNS=0

# Run the Gradle task in a loop
for ((i=1; i<=$NUM_RUNS; i++)); do
  echo "Executing Test (Run $i/$NUM_RUNS)..."
  ./gradlew :micronaut-gcp-pubsub:test --tests io.micronaut.gcp.pubsub.integration.SubscriberShutdownSpec --rerun || EXIT_STATUS=$?

  if [ $EXIT_STATUS -ne 0 ]; then
	exit $EXIT_STATUS
  fi
done

exit $EXIT_STATUS
```

