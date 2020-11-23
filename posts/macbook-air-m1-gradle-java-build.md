---
title:  Java Build Benchmarks with Apple M1
date_published: 2020-11-21T12:15:00+01:00
date_modified: 2020-11-21T12:15:00+01:00
summary: I compare an iMac Pro, Macbook Pro, Macbook Air M1 for a common Gradle Java build.
banner_image: https://images.sergiodelamo.com/macbook-air-m1-gradle-java-build.png
---

# [%title]

[%date]

Tags: #java #applesilicon #mac

I compare an iMac Pro, Macbook Pro, [Macbook Air 2020 M1](https://sergiodelamo.com/blog/macbook-air-m1.html) for a common Gradle Java build.

### iMac Pro 2017

| Characteristic | Value |
|:--- |:--- |
| Model Name | iMac Pro 2017 |
| Processor | 3GHz 10-Core Intel Xeon W |  
| Memory | 32 GB 2666 MHz DDR4  |
| Total Number of Cores | 10 | 
| Operating System | Mac OS X 10.15.7 (Mojave) |

### Macbook Pro 2014

| Characteristic | Value |
|:--- |:--- |
| Model Name | MacBook Pro (Retina, 13-inch, Mid 2014) |
| Processor | 3 GHz Dual-Core Intel Core i7 |
| Memory | 16 GB 1600 MHz DDR3 |
| Total Number of Cores | 2 |
| Operating System | Mac OS X 10.15.7 (Mojave) |

### Macbook Air 2020 M1

| Characteristic | Value |
|:--- |:--- |
| Model Name | MacBook Air |
| Processor | M1 2020 |
| Memory | 16 GB  |
| CPU cores	| 8 cores |	
| Operating system | Mac OS X 10.16	|

## Java version

Micronaut is still JDK 8 compatible. I run the tests with Corretto 8 JDK. I installed it via [SDKMan](https://sdkman.io). 

```
% sdk install java 8.0.275-amzn
```

| Characteristic | Value |
|:--- |:--- |
| Java runtime | Amazon.com Inc. OpenJDK Runtime Environment 1.8.0_275-b01 |
| Java VM | Amazon.com Inc. OpenJDK 64-Bit Server VM 25.275-b01 (mixed mode) |


## Code sample

I use [Micronaut Security](https://github.com/micronaut-projects/micronaut-security) module.

## Command

I run the Gradle `build` task which compiles the code, runs the tests and checksytle. It is one of the most common tasks. 

```
% ./gradlew clean;./gradlew -Dtestcontainers=false build 
    --no-daemon
    --no-build-cache 
    -x dependencyUpdates 
    --parallel 
    --scan
```

I supply some arguments: 

-  I use  `--no-build-cache` because I want to have an easy way to compare between machines. I use [Gradle build cache](https://docs.gradle.org/current/userguide/build_cache.html) in my projects. It is a great feature. You should use it.
- I disable the [Gradle Daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html) with `--no-daemon`. For the same reasons as in the previous item, to compare different machines in the fairest  way.
- I skip the task `dependencyUpdates` as it may add inconsistency to the test. 
- I ignore tests which required Docker (which does not run with Apple M1), hence the argument `-Dtestcontainers=false`.

- I use [Gradle Parallel execution](https://docs.gradle.org/nightly/userguide/performance.html#parallel_execution) with `--parallel` flag.

> Most builds consist of more than one project and some of those projects are usually independent of one another. Yet Gradle will only run one task at a time by default, regardless of the project structure (this will be improved soon). By using the --parallel switch, you can force Gradle to execute tasks in parallel as long as those tasks are in different projects.

## Results

| Computer | Duration | Gradle Build Scan |
| :--- | :--- | :--- |
| iMac Pro - 3GHz 10 Core Intel Xeon W | 1m 44s | [Gradle Build Scan](https://gradle.com/s/zvqk5oaaovr3m) |
| Macbook Air M1 8 Core | 2m 54s | [Gradle Build Scan](https://gradle.com/s/gabwdvawzygoy) |
| Macbook Pro 2 Core 3GHz I7 | 4m 33s | [Gradle Build Scan](https://gradle.com/s/xgunasq3rcfgi) |


![](https://images.sergiodelamo.com/macbook-air-m1-gradle-java-build.png)
 
## Caveats

This is not an empirical comparison. The goal of the post is to put in numbers my feelings. Java builds are fast in a Macbook Air M1. 

These benchmarks should have been calculated with the [Gradle Profiler](https://github.com/gradle/gradle-profiler). However, I was not able to get the Gradle Profiler to run with the M1 computer. 

Don't consider this post as a measurement Micronaut security's build performance. 

Neither, it is intended to showcase of Gradle's performance. It will be unfair for Gradle. I explicitly disabled several features (Build Cache, Daemon) which make Gradle builds much faster. 

## Conclusion

- My 2017 iMac Pro, a powerhouse, still beats an Macbook Air M1. 
- The Macbook Air M1 smokes old Macbook Pros.
- Macbook Air M1 will get you similar Java build times for current top of the line Macbook Pros.

The exciting thing is that Java builds are not taking full advantage of the transition. It is [Rosetta emulation](https://developer.apple.com/forums/thread/651123): 

> the Java runtime support will run under Rosetta 2 which will translate Intel compiled code to run on Apple Silicon. The Java Virtual Machine will need to be recompiled for Apple Silicon to fully utilize its power, but Java based applications should continue to run as expected on both Intel and Apple Silicon hardware.

Once the Java story improves in Apple silicon, I will write about it. I am thrilled. 