---
title: Micronaut Euro Rates Library
date: Sep 3, 2020
speakerdeck: 
summary:  Java library to consume Euro foreign exchange rates XML feed published by the European Central bank.
---

# [%title]

[%date]

Tags: #micronaut

I have published a [Micronaut](https://micronaut.io) Java library to consume the [Euro foreign exchange reference rates](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html) XML feed published by the European Central bank. 

The library has a transitive dependency to [Micronaut Jackson XML](https://micronaut-projects.github.io/micronaut-jackson-xml/latest/guide/index.html) and because of that it is able to create a Micronaut Declarative HTTP Client to consume an XML payload such as:

```xml
<gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
<gesmes:subject>Reference rates</gesmes:subject>
<gesmes:Sender>
<gesmes:name>European Central Bank</gesmes:name>
</gesmes:Sender>
<Cube>
<Cube time="2020-09-02">
<Cube currency="USD" rate="1.1861"/>
<Cube currency="JPY" rate="126.00"/>
<Cube currency="BGN" rate="1.9558"/>
<Cube currency="CZK" rate="26.338"/>
<Cube currency="DKK" rate="7.4412"/>
<Cube currency="GBP" rate="0.88840"/>
<Cube currency="HUF" rate="358.77"/>
<Cube currency="PLN" rate="4.4186"/>
<Cube currency="RON" rate="4.8423"/>
<Cube currency="SEK" rate="10.3065"/>
<Cube currency="CHF" rate="1.0799"/>
<Cube currency="ISK" rate="164.50"/>
<Cube currency="NOK" rate="10.4060"/>
<Cube currency="HRK" rate="7.5335"/>
<Cube currency="RUB" rate="88.0675"/>
<Cube currency="TRY" rate="8.7588"/>
<Cube currency="AUD" rate="1.6171"/>
<Cube currency="BRL" rate="6.4205"/>
<Cube currency="CAD" rate="1.5495"/>
<Cube currency="CNY" rate="8.0976"/>
<Cube currency="HKD" rate="9.1925"/>
<Cube currency="IDR" rate="17489.04"/>
<Cube currency="ILS" rate="3.9901"/>
<Cube currency="INR" rate="86.8225"/>
<Cube currency="KRW" rate="1408.07"/>
<Cube currency="MXN" rate="25.9150"/>
<Cube currency="MYR" rate="4.9176"/>
<Cube currency="NZD" rate="1.7541"/>
<Cube currency="PHP" rate="57.665"/>
<Cube currency="SGD" rate="1.6153"/>
<Cube currency="THB" rate="37.095"/>
<Cube currency="ZAR" rate="19.9154"/>
</Cube>
</Cube>
</gesmes:Envelope>
```
 
 and bind it to a JavaBean: 
 
 ```java
 EuroRatesApi api = ...
 GesmesEnvelope envelope = api.currentReferenceRates().blockingGet()
 ```
  
[Micronaut Euro Rates documentation](https://sdelamo.github.io/eurorates/index.html)
