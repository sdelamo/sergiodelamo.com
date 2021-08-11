---
title: Test Micronaut POJOs are annotated with @Introspected
summary: I always annotate my model classes with @Introspected. It is easy to test that you don't forget the annotation.
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-05-17T15:48:21+01:00
date_modified: 2021-05-17T15:48:21+01:00
keywords:micronaut,spock,test
---

# [%title]

I always annotate my model classes with `@Introspected`. It is easy to test you don't forget the annotation.

Given a [POJO](https://en.wikipedia.org/wiki/Plain_old_Java_object): 


```java
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Introspected
public class EuroExchangeResponse {

    @NonNull
    @NotNull
    private BigDecimal rate;

    @NonNull
    @NotNull
    private Currency target;

    @NonNull
    @NotNull
    private LocalDate date;

    public EuroExchangeResponse(@NonNull BigDecimal rate,
                                @NonNull Currency target,
                                @NonNull LocalDate date) {
        this.rate = rate;
        this.target = target;
        this.date = date;
    }

    // Getters & Setters
}
```

I write a [Spock specification to verify that no exception is thrown](https://blog.mrhaki.com/2011/01/spocklight-check-for-exceptions-with.html) when I attempt to retrieve the [Bean Introspection](https://docs.micronaut.io/latest/guide/#introspection):


```groovy
import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification

class EuroExchangeResponseSpec extends Specification {

    void "EuroExchangeResponse is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(EuroExchangeResponse)

        then:
        noExceptionThrown()
    }
}
```


