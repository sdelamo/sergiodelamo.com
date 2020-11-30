---
title: How to validate an email address in Android
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2017-02-06T08:39:00+01:00
date_modified: 2017-02-06T08:39:00+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #android

```java
String email = "me@email.com";
if ( android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
// VALID EMAIL
}
```