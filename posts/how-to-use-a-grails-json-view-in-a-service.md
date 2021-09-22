---
title: How to use a JSON View in a Grails Service
summary: You can render a model into JSON with a JSON View within a Grails service. 

author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-09-22T14:56:34+01:00
date_modified: 2021-09-22T14:56:34+01:00
keywords:grails
---

# [%title]

You can render a model into JSON with a [JSON View](http://views.grails.org/latest/#_json_views) within a [Grails](https://grails.org) service: 

Given the following view:

`grails-app/views/apiContact/_contact.gson`

```groovy
import example.Contact

model {
    Contact contact
}
json {
    fullname contact.name
}
```

You can create a service such as: 

```groovy
package example

import grails.plugin.json.view.JsonViewTemplateEngine
import groovy.text.Template
import org.springframework.beans.factory.annotation.Autowired

class JsonViewRenderingService {
    @Autowired
    JsonViewTemplateEngine jsonViewTemplateEngine

    Writable renderWritable(String templatePath, Map<String, Object> model) {
        jsonViewTemplateEngine.resolveTemplate(templatePath)
            .make(model)
    }

    String render(String templatePath, Map<String, Object> model) {
        renderWritable(templatePath, model)
            .writeTo(new StringWriter())
            .toString()
    }
}
```

Which you can call from other Grails Artifacts: 

```groovy
jsonViewRenderingService.render('/apiContact/_contact',
[contact: new Contact(name: 'Sergio')])
```

Kudos to [@virtualdogbert](https://twitter.com/virtualdogbert) for this trick.



