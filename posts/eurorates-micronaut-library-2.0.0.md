---
title: Version 2.0.0 of Eurorates Micronaut Library released
banner_image: https://images.sergiodelamo.com/eurorates-micronaut-library-2.0.0.png
summary: Micronaut 3 and reactive streams agnostic
date_published: 2021-08-20T17:06:51+01:00
keywords:micronaut
---

# [%title]

## Reactive Streams implementation agnostic

[Micronaut framework 3](https://micronaut.io/2021/08/18/micronaut-framework-3-released/) no longer exposes a Reactive Streams implementation :

> Previous releases of the Micronaut framework included RxJava2 as a transitive dependency, and RxJava2 was the reactive streams implementation used to implement many features within the Framework. The Micronaut framework now no longer exposes any reactive streams implementation by default. In addition, all usages of RxJava2 internally have been replaced with Project Reactor.

It makes sense. Micronaut framework is build agnostic. You can use Gradle or Maven. Micronaut framework is JVM programming language agnostic. You can code in Java, Groovy or Kotlin. Now, it is reactive streams agnostic. You choose your favourite poison.

## Eurorates updated to Micronaut 3

I have released version 2.0.0 of [Eurorates](https://github.com/sdelamo/eurorates), a tiny [Micronaut](https://micronaut.io) Java library which helps you consume the [Euro foreign exchange reference rates](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html). This new mayor release updates to Micronaut 3 and changes the API to be Reactive Streams implementation agnostic. The library does not expose Rx Java 2 or any other reactive streams implementation library as transitive dependency anymore.

The main interface used to be: 

```java
public interface EuroRatesApi {
     Single<GesmesEnvelope> currentReferenceRates();
     Single<GesmesEnvelope> historicalReferenceRates();
     Single<GesmesEnvelope> last90DaysReferenceRates();
 }
``` 

Now, it is: 


```java
public interface EuroRatesApi {
     @SingleResult
     Publisher<GesmesEnvelope> currentReferenceRates();

     @SingleResult
     Publisher<GesmesEnvelope> historicalReferenceRates();

     @SingleResult
     Publisher<GesmesEnvelope> last90DaysReferenceRates();
 }
``` 

The API exposes a `org.reactivestreams.Publisher`. Moreover, I annotate the methods with Micronaut annotation [`@SingleResult`](https://docs.micronaut.io/latest/api/io/micronaut/core/async/annotation/SingleResult.html). 

> Annotation that can be used to describe that an API emits a single result even if the return type is a Publisher.

We encourage library authors to write reactive streams agnostic libraries.

I use this library myself, for example, in Telegram bot: [@ForeignExchangeRatesBot](https://exchangeratesbot.com).


