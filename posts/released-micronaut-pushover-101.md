---
title: Micronaut Pushover released
summary: A Java library to consume the Pushover API
banner_image: https://images.sergiodelamo.com/micronaut-pushover-released.png
date_published: 2021-09-08T05:58:18+01:00
keywords:micronaut,pushover
---

# [%title]

I've released  a [Micronaut](https://micronaut.io) Java library to consume the [Pushover API](https://pushover.net/api). It is built with the [Micronaut](https://micronaut.io) Framework and you can use it in a Micronaut app or as a standalone library.

## Installation

To use it with [Gradle](https://gradle.org):

```groovy
dependencies {
    ...
    ..
    implementation 'com.softamo:pushover:1.0.1'
}
```

To use it with [Maven](https://maven.apache.org):

```xml
<dependency>
	<groupId>com.softamo</groupId>
	<artifactId>pushover</artifactId>
	<version>1.0.1</version>
	<type>pom</type>
</dependency>
```

## Usage

### Micronaut Applications

If you want to use the library in Micronaut application, register applications and users via configuration: 

```yaml
pushover:
	applications:
		l3-37:
			token: 'T6xoNWc7zboEppeeM69tMZsCNkdRqU'
	users:
		sdelamo:
			key: 's2HkfXVenEeMJ2MBwqDZrhAXpg7uzK',
```

Then you can send messages from an application to a user:

```java
@Controller
public class HomeController {
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	private final PushoverApplication application;
	private final PushoverUser user;
	private final Scheduler scheduler;

	HomeController(@Named(TaskExecutors.IO) ExecutorService executorService,
				   @Named("l3-37") PushoverApplication application,
				   @Named("sdelamo") PushoverUser user) {
		this.scheduler = Schedulers.fromExecutorService(executorService);
		this.application = application;
		this.user = user;
	}
	
	@Get
	@Produces(MediaType.TEXT_PLAIN)
	String index() {
		Message message = Message.builder("Hello World").sound(Sound.ALIEN).build();
		Mono.from(application.send(user, message))
				.subscribeOn(scheduler)
				.subscribe(response -> {
					if (response.getStatus().equals(1)) {
						LOG.trace("your notification has been received and queued. Request {}", response.getRequest());
					} else {
						LOG.trace("your notification could not be delivered. status {}", response.getStatus());
					}
				});
		return "Hello world";
	}
}
```

Moreover, For Micronaut Applications you can annotate your methods with `@PushoverMessage`

```java
...
..
.
@Controller
public class HomeController {

	@PushoverMessage(value = "User greeted",
			url = "https://sergiodelamo.com",
			sound = Sound.ALIEN,
			appName = "l3-37"
			userName = "sdelamo",
			urlTitle = "blog")
	@Get
	@Produces(MediaType.TEXT_PLAIN)
	String index() {
		return "Hello world";
	}
}
```

If you have only one application and user configured, you can skip `appName` and `userName` members.

```java
...
..
.
@Controller
public class HomeController {
	@PushoverMessage(value = "User greeted",
			url = "https://sergiodelamo.com",
			sound = Sound.ALIEN,
			urlTitle = "blog")
	@Get
	@Produces(MediaType.TEXT_PLAIN)
	String index() {
		return "Hello world";
	}
}
```

### Without Micronaut

You can use the library without a Micronaut Application Context. In that case, you can do:

```java
PushoverUser user = new PushoverUser() {
	@Override
	public String getKey() {
		return "s2HkfXVenEeMJ2MBwqDZrhAXpg7uzK";
	}
	@Override
	public String getName() {
		return "sdelamo";
	}
};
PushoverApplication application = new PushoverApplication(new PushoverApplicationConfiguration() {
	@Override
	public String getToken() {
		return "T6xoNWc7zboEppeeM69tMZsCNkdRqU";
	}
	@Override
	public String getName() {
		return "l3-37";
	}
},new ManualPushoverHttpClient()); 
Message message = Message.builder("Hello World").sound(Sound.ALIEN).build();
Response result = Mono.from(application.send(user, message)).block()
```

- [Code Repository](https://github.com/sdelamo/pushover)
- [Releases](https://github.com/sdelamo/pushover/releases)

I hope you find it as useful as I do.
