title: Pass system property in a Gradle Task
date: March 25, 2019 10:00
---

# [%title]

[%date]

If you created a [Micronaut](https://micronaut.io) App with Gradle (the default build tool) used by Micronaut CLI, you may want to start your app in a particular Micronaut environment. You can supply an environment with the system property `micronaut.environments`. Modify your build.gradle file

```groovy
run {
systemProperty "micronaut.environments", System.getProperty('micronaut.environments')
}
```

Then you can start the app with:

```bash
$ ./gradlew -Dmicronaut.environments=dev run
```
