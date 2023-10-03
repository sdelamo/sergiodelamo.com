---
title: System Stubs
summary: > System Stubs is used to test code which depends on methods in java.lang.System.
date_published: 2023-10-03T11:58:16+01:00
keywords:java,junit5
external_url: https://github.com/webcompere/system-stubs#system-stubs
---

# [%title]

[%summary]


```java
@ExtendWith(SystemStubsExtension.class)
class WithEnvironmentVariables {
	@SystemStub
	private EnvironmentVariables variables = new EnvironmentVariables("input", "foo");

	@Test
	void hasAccessToEnvironmentVariables() {
		assertThat(System.getenv("input")).isEqualTo("foo");
	}

	@Test
	void changeEnvironmentVariablesDuringTest() {
		variables.set("input", "bar");
		assertThat(System.getenv("input")).isEqualTo("bar");
	}
}
```

