---
title: Debug Grails Plugins load order
date: Jul 05, 2017 14:40
---

# [%title]

[%date]

Tags: #grails

Grails documentation talks about Understanding Plugin Load Order and controlling it with the use of loadBefore and loadAfter properties.

It is easy to debug the plugin load order as well. Add this line to your grails-app/conf/logback.groovy file.

`logger('grails.plugins.DefaultGrailsPluginManager', INFO, ['STDOUT'], false)`




