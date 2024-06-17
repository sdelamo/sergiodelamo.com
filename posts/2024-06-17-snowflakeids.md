---
title: Snowflake IDs
summary: Snowflakes are sortable unique identifiers. They are 64 bits in binary.
date_published: 2024-06-17T15:31:21+01:00
keywords:uid,snowflake
---

# [%title]

I have been researching [Snowflake IDs](https://en.wikipedia.org/wiki/Snowflake_ID) and [how Snowflake IDs work](https://www.youtube.com/watch?v=aLYKd7h7vgY): 

> Snowflake IDs, or snowflakes, are a form of unique identifier used in distributed computing. [The format was created by Twitter](https://blog.x.com/engineering/en_us/a/2010/announcing-snowflake) and is used for the IDs of tweets.

> Snowflakes are 64 bits in binary. The first 41 bits are a timestamp, representing milliseconds since the chosen epoch. The next 10 bits represent a machine ID, preventing clashes. Twelve more bits represent a per-machine sequence number, to allow creation of multiple snowflakes in the same millisecond. The final number is generally serialized in decimal.

A key characteristic, as with [KSUID](https://sergiodelamo.com/blog/ksuid.html), is that they are sortable: 
 
> Snowflakes are sortable by time, because they are based on the time they were created.[2] Additionally, the time a snowflake was created can be calculated from the snowflake. This can be used to get snowflakes (and their associated objects) that were created before or after a particular date.

## Java Libraries

I found several Java implementations: 

[`phxql/snowflake-id`](https://github.com/phxql/snowflake-id), which is licensed under [LGPL (GNU Lesser General Public License 3.0)](https://www.gnu.org/licenses/lgpl-3.0.en.html). It seems active. 

I also found this [java implementation](https://github.com/callicoder/java-snowflake), which unfortunately it does not specify a license.

I found an [implementation](https://github.com/apache/marmotta/blob/master/libraries/kiwi/kiwi-triplestore/src/main/java/org/apache/marmotta/kiwi/generator/SnowflakeIDGenerator.java) and [test](https://github.com/apache/marmotta/blob/master/libraries/kiwi/kiwi-triplestore/src/test/java/org/apache/marmotta/kiwi/test/generator/SnowflakeTest.java) in [Apache Marmotta](https://marmotta.apache.org)
which is licensed with [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0). 

It seems [Discord4J](https://discord4j.com) contains also a Snowflake ID implementation. 