---
title: How to validate an email address in Android
date_published: 2017-02-06T08:39:00+01:00
keywords:android
---

# [%title]

```java
String email = "me@email.com";
if ( android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
// VALID EMAIL
}
```