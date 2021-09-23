---
title: Swift vs Groovy - Types
date_published: 2016-10-11T06:05:00+01:00
keywords:groovy,swift
---

# [%title]

## Getting started Swift types

- **Int** Integer, whole number that does not containing any floating point information.
- **Float** 32bit number with floating point information
- **Double** 64bit number with floating point information
- **Bool** true or false
- **String** represents text
- **Character** Single unicode character

There are more types but those are the most common in Swift.

## Type Naming

Swift Types and the types you defined should be UpperCamelCase. Same in Groovy. However, in Groovy we have access to java primitives short, int , long, float, double, byte, char, boolean etc. Those are lowercase.

## Type inference

If we don't explicitly defined a type both Swift and Groovy will try to infer it.

**Swift**

```swift
let i = 42 // Int
let d = 42.5 // Double
```

**Groovy**

```groovy
def i = 42 // java.lang.Integer
def d = 42.5 // java.math.BigDecimal
````

Define type explicitly

```swift
let i: Int = 42
let d: Double = 42.5
```

**Groovy**

```groovy
Integer i = 42
Double d = 42.5
```

Type conversion

**Swift**

```swift
let d: Double = 42.5
let i: Int = Int(d) // 42
```

**Groovy**

```groovy
final Double d = 42.5 // java.lang.Double
final Integer i = (Integer)42.5 // 42 java.lang.Integer
final Integer x = 42.5 as Integer // 42 java.lang.Integer
```

as keyword in Swift

If the default inferred type is not what you are looking for, you convert value to type with the as keyword

```swift
let x = 42 as Double
```
