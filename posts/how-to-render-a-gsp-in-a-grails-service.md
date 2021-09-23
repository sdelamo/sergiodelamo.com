---
title: How to render a GSP in a Grails Service
summary: We can use groovyPageRenderer instance to render GSP views and templates outside a controller
banner_image: https://images.sergiodelamo.com/gsp-in-a-grails-service.png
date_published: 2021-09-22T15:18:11+01:00
keywords:grails,gsp
---

# [%title]

You can render a model with a [GSP template](https://gsp.grails.org) within a [Grails](https://grails.org) service: 

Given the following view:

`grails-app/views/contact/_contact.gsp`

```groovy
<h1>${contact.name}</h1>
```

You can create a service such as: 

```groovy

import grails.gsp.PageRenderer
import groovy.transform.CompileStatic

@CompileStatic
class GspRenderingService {
    PageRenderer groovyPageRenderer

    String render(String templatePath, Map<String, Object> model) {
        groovyPageRenderer.render(view: templatePath, model: model)
    }
}
```

Which you can call from other Grails Artifacts: 

```groovy
gspRenderingService.render('/contact/_contact',
[contact: new Contact(name: 'Sergio')])
```

[@mrhaki](https://twitter.com/mrhaki) has a more comprehensive blog post ([Grails Goodness: Render GSP Views And Templates Outside Controllers](https://blog.mrhaki.com/2012/03/grails-goodness-render-gsp-views-and.html)) about this feature.