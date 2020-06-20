title: Debug Grails Plugins load order
date: Jul 5, 2017
---

# [%title]

[%date] #grails

Grails documentation talks about Understanding Plugin Load Order and controlling it with the use of loadBefore and loadAfter properties.

It is easy to debug the plugin load order as well. Add this line to your grails-app/conf/logback.groovy file.


```groovy
logger('grails.plugins.DefaultGrailsPluginManager', INFO, ['STDOUT'], false)
```

