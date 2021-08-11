---
title: Grails Programmer : How to change the default port of a Grails 3 App
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png
date_published: 2016-02-27T06:07:00+01:00
date_modified: 2016-02-27T06:07:00+01:00
keywords:grails
---

# [%title]

By default a Grails 3 app will start in the port 8080. It is easy however to configure a different port. Edit `grails-app/conf/application.yml` and add the next snippet at the bottom of the file.

```
---
server:
  port: 8090
```

Next time you run the app, it will listen in the port 8090.

