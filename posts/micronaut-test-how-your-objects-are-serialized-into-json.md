---
title: Testing JSON serialization with Micronaut
summary: In Micronaut applications, you can control JSON serialization with Jackson annotations. Inject ObjectMapper into your tests to verify it.
date_published: 2021-05-07T16:44:51+01:00
keywords:micronaut,spock,jackson,test
---

# [%title]

The following enum uses [Jackson](https://github.com/FasterXML/jackson)'s `@JsonValue` annotation to render as lowercase string when serialized as JSON.

```java
package example.micronaut;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

public enum NoteKind {
	TRIX;

	@JsonValue
	@Override
	public String toString() {
		return this.name().toLowerCase(Locale.ENGLISH);
	}
}
```

We can test it with [Spock](https://spockframework.org) by injecting the `ObjectMapper` into your test:

```groovy
package example.micronaut

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest(startApplication = false)
class NoteKindSpec extends Specification {

	@Inject
	ObjectMapper objectMapper

	void "NoteKind is render as lowercase when serialized as JSON"() {
		given:
		MockPojo pojo = new MockPojo(kind: NoteKind.TRIX)
		
		expect:
		objectMapper.writeValueAsString(pojo) == '{"kind":"trix"}'
	}

	static class MockPojo {
		NoteKind kind
	}
}
```