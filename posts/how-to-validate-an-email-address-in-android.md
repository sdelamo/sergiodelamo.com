---
title: How to validate an email address in Android
date: Feb 06, 2017 08:39
---

# [%title]

[%date]

Tags: #android

```java
String email = "me@email.com";
if ( android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
// VALID EMAIL
}
```