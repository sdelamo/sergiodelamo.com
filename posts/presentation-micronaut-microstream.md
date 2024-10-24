---
title: Micronaut® Framework and Microstream Java-native persistence engine
summary: This talk introduces Microstream integration with the Micronaut Framework.
date_published: 2022-04-17T17:28:31+01:00
keywords:presentation,micronaut,turbo
---

# [%title]

I did a similar presentation at
[JCON World 2023](/blog/jconworld-2023-micronaut-eclipsestore.html), and
[JCON 2023](/blog/jcon-2023-micronaut-and-microstream.html).

[Sergio del Amo](https://sergiodelamo.com) introduces [Microstream integration](https://micronaut-projects.github.io/micronaut-microstream/snapshot/guide/) with the Micronaut® Framework in this session.

[Microstream](https://microstream.one) is an Object-Graph Persistence solution that allows ultra-fast in-memory data processing with pure Java.

> - MicroStream is a Java-native object graph persistence engine for storing any complex Java object graph or any single subgraph and restoring it in RAM at any time by using a fundamentally new serialization concept designed from scratch. 
> - With MicroStream, you can restore in RAM on demand the entire object graph, partial subgraphs, or only single objects.
> - Beyond serialization, MicroStream is ACID transaction safe, can handle your class changes and provides a garbage collector for the storage, multi-threaded IO, and connectors for various data storages.

This session will teach you how to use Microstream as your persistence engine in a Micronaut Application. Micronaut Microstream integration allows you to run several Microstream instances in the sample application, simplifies configuration, storage operations, metrics, health, etc.

Moreover, you can use Microstream as a cache implementation for the Micronaut Framework. 

Attendees will discover how the Micronaut Framework and Microstream are a perfect combination to build ultra-fast applications without leaving the confines of Java.

## Target Attendees

Everyone is welcome. However, we recommend attendees have at least a working familiarity with web development, HTTP, Java, and JVM development frameworks. Experience with Micronaut is a plus, but not required.

## Technical Requirements

For the lab exercises, you will need JDK 11 and IntelliJ IDEA Community Edition or Ultimate.