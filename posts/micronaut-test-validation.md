---
title: Test Micronaut POJOs contain validation annotations
summary: I like to define validation annotations for Micronaut application's models.
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-05-17T16:09:09+01:00
date_modified: 2021-05-17T16:09:09+01:00
---

# [%title]


By [%author.name] - [%date_published]

Tags: #micronaut #test #spock

The following POJO's fields are annotated with `javax.validation.constraints.NotNull` to indicate they are required. 


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

    @NonNull
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(@NonNull BigDecimal rate) {
        this.rate = rate;
    }

    @NonNull
    public Currency getTarget() {
        return target;
    }

    public void setTarget(@NonNull Currency target) {
        this.target = target;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDate date) {
        this.date = date;
    }
}
``` 

I typically write such a test to verify that I did not forget to add the validation annotations.

```groovy
import io.micronaut.context.ApplicationContext
import io.micronaut.core.beans.BeanIntrospection
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validator
import java.time.LocalDate

class EuroExchangeResponseSpec extends Specification {

    @AutoCleanup
    @Shared
    ApplicationContext applicationContext = ApplicationContext.run()

    @Shared
    Validator validator = applicationContext.getBean(Validator)

    void "No constraint violations are found for valid model"() {
        given:
        EuroExchangeResponse obj = valid()

        when:
        Set<ConstraintViolation<EuroExchangeResponse>> violations = validator.validate(obj)

        then:
        violations.isEmpty()
    }

    void "rate cannot be null"() {
        given:
        EuroExchangeResponse obj = valid()

        when:
        obj.rate = null
        Set<ConstraintViolation<EuroExchangeResponse>> violations = validator.validate(obj)

        then:
        !violations.isEmpty()
    }

    void "date cannot be null"() {
        given:
        EuroExchangeResponse obj = valid()

        when:
        obj.date = null
        Set<ConstraintViolation<EuroExchangeResponse>> violations = validator.validate(obj)

        then:
        !violations.isEmpty()
    }

    void "target cannot be null"() {
        given:
        EuroExchangeResponse obj = valid()

        when:
        obj.target = null
        Set<ConstraintViolation<EuroExchangeResponse>> violations = validator.validate(obj)

        then:
        !violations.isEmpty()
    }

    static EuroExchangeResponse valid() {
        new EuroExchangeResponse(0.861500, Currency.GBP, LocalDate.of(2021,05,17))
    }

}
``` 

