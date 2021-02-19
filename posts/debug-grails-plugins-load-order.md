---
title: Debug Grails Plugins load order
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2017-07-05T14:40:00+01:00
date_modified: 2017-07-05T14:40:00+01:00
---

# [%title]

[%author.name] [%date_published]

Tags: #grails

Grails documentation talks about Understanding Plugin Load Order and controlling it with the use of loadBefore and loadAfter properties.

It is easy to debug the plugin load order as well. Add this line to your grails-app/conf/logback.groovy file.

`logger('grails.plugins.DefaultGrailsPluginManager', INFO, ['STDOUT'], false)`




