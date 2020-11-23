---
title: Grails Programmer : How to change the server url of a Grails 3 App?
date_published: 2016-03-02T09:00:00+01:00
date_modified: 2016-03-02T09:00:00+01:00
---

# [%title]

[%date]

Tags: #grails

Sometimes you need to configure the exact server url (via domain name or ip address) of your Grails app. It is easy do it with a small configuration in your `grails-app/conf/application.yml`

```yaml
---
environments:
    development:
        grails:
            serverURL: http://192.168.1.5:8080
    test:
        grails:
            serverURL: http://localhost:8080
    production:
        grails:
            serverURL: http://mydomain.com
```

