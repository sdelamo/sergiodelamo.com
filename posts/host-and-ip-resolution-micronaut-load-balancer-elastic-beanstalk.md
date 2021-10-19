---
title: Host and IP Resolution in a Micronaut application with Load Balancer and Elastic Beanstalk 
summary: Leverage Micronaut APIs to resolve Host and IP when running behind a Elastic Load Balancer
date_published: 2021-04-25T10:28:04+01:00
keywords:micronaut,aws,elasticbeanstalk,elasticloadbalancing
---

# [%title]

When you run your application behind a [Elastic Load Balancer](https://aws.amazon.com/elasticloadbalancing), a [Classic Load Balancer](https://aws.amazon.com/elasticloadbalancing/classic-load-balancer/) or an [Application Load Balancer](https://aws.amazon.com/elasticloadbalancing/application-load-balancer/), AWS decorates your request with [X-Forward HTTP Headers](https://en.wikipedia.org/wiki/X-Forwarded-For). 

![](https://images.sergiodelamo.com/elb-elasticbeanstalk-x-forwarded-headers.png)

In a Micronaut application, you may need to resolve the domain name of your application, for example when using OAuth 2.0 for a `redirect_uri`, or restrict an endpoint to a range of IP Addresses, for example a Stripe's callback.  

Micronaut® framework contains two APIs:

- `HttpHostResolver` to resolve the host
- `HttpClientAddressResolver` to resolve the IP. 

Given the following controller:

_`src/main/java/example/micronaut/HeadersController.java`_
```java
package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.server.util.HttpClientAddressResolver;
import io.micronaut.http.server.util.HttpHostResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HeadersController {

	private static final Logger LOG = LoggerFactory.getLogger(HeadersController.class);

	private final HttpHostResolver httpHostResolver;
	private final HttpClientAddressResolver httpClientAddressResolver;

	public HeadersController(HttpHostResolver httpHostResolver,
							 HttpClientAddressResolver httpClientAddressResolver) {
		this.httpHostResolver = httpHostResolver;
		this.httpClientAddressResolver = httpClientAddressResolver;
	}

	@Get
	public Map<String, Object> index(HttpRequest<?> request) {
		for (String name : request.getHeaders().names()) {
			if (LOG.isInfoEnabled()) {
				LOG.info("H {}: {}", name, request.getHeaders().get(name));
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("ip", httpClientAddressResolver.resolve(request));
		result.put("resolved_host", httpHostResolver.resolve(request));
		return result;
	}
}

```

If I run this application locally, I get:

```bash
% curl localhost:8080
{"ip":"0:0:0:0:0:0:0:1","resolved_host":"http://localhost:8080"}
```

with logs:
```bash
H Host: localhost:8080
H User-Agent: curl/7.64.1
H Accept: */*
```

To resolve the Host and IP in [Elastic Beanstalk](https://aws.amazon.com/elasticbeanstalk/) behind an Application Load Balancer add: 

_`src/main/resources/application-ec2.yml`_
```yaml
micronaut:
  server:
	host-resolution:
	  protocol-header: X-Forwarded-Proto
	  port-header: X-Forwarded-Port
	  host-header: Host
	client-address-header: X-Real-IP
```

EC2 environment is automatically detected, because of that I typically use `application-ec2.yml` for such configuration.

If you call your application's endpoint running in Elastic Beanstalk you get:

```bash
% curl http://micronaut.example
{"ip":"79.150.119.41","resolved_host":"http://micronaut.example"}
```

with logs:

```bash
H Connection: upgrade
H Host: micronaut.example
H X-Real-IP: 172.31.91.156
H X-Forwarded-For: 79.150.119.41, 172.31.91.156
H X-Forwarded-Proto: http
H X-Forwarded-Port: 80
H X-Amzn-Trace-Id: Root=1-60865015-39f63e107ca0f25410b0e66e
H User-Agent: curl/7.64.1
H Accept: */*
```


