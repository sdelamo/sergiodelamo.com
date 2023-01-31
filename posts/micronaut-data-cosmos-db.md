---
title: Micronaut Data and CosmosDB
summary: Since Micronaut Framework 3.8.0, Micronaut Data offers a flavor for [Azure CosmosDB] which supports compile-time generated repositories and projection queries.
date_published: 2023-01-31T19:36:59+01:00
keywords:micronaut,presentation,microstream
---

# [%title]

Micronaut Framework is a modern JVM and modular framework. One of its most powerful modules is Micronaut Data. Micronaut Data is a database access toolkit that uses Ahead of Time (AoT) compilation to pre-compute queries for repository interfaces. A thin, lightweight runtime layer executes those queries.

Since Micronaut Framework 3.8.0, Micronaut Data offers a flavor for [Azure CosmosDB] which supports some of the features Micronaut Data developers love and expect, including:

- Repositories compile-time generated and projection queries
- Attribute converters
- Optimistic locking

In this talk, Sergio del Amo introduces Micronaut Data Azure CosmosDB integration and shows you how easy it is to get started. 