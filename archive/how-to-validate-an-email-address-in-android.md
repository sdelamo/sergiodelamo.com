title: How to validate an email address in Android
date: Feb 6, 2017
---

# [%title]

[%date]

```java
String email = "me@email.com"; if ( android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) { // VALID EMAIL }
```
