---
title: Localized error message for a Grails Validateable POGO's field
summary: I often decorate my Grails controllers with a Trait which uses messageSource.
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-09-22T14:10:02+01:00
date_modified: 2021-09-22T14:10:02+01:00
keywords:grails
---

# [%title]

I often decorate my [Grails](https://grails.org) Controllers with [Traits](https://docs.groovy-lang.org/next/html/documentation/core-traits.html). I often use is the following trait:

```groovy
@CompileStatic
trait ValidateableMessageSource {
    MessageSource messageSource

    Optional<String> errorMessage(Validateable validateable,
                                  String fieldName) throws NoSuchMessageException {
        FieldError fieldError = validateable.errors?.getFieldError(fieldName)
        if (!fieldError) {
            return Optional.empty()
        }
        Optional.of(messageSource.getMessage(fieldError, LocaleContextHolder.locale))
    }
}
```

I use a POGO to bind the HTTP Request body.

```groovy
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class ContactSave implements Validateable {
    String name

    static constraints = {
        name blank: false
    }
}
```

This is how I use the trait in a controller:

```groovy
class ContactController implements ValidateableMessageSource {
    ...
    
    def save(ContactSave contactSave) {
        if (contactSave.hasErrors()) {
            String invalid = errorMessage(contactSave, "name").orElse(null)
            ContactCreate contactCreate = new ContactCreate(name: contactSave.name,
                                                        nameInvalid: invalid)
            render view: 'create', model: [contact: contactCreate]
            return
        }
        String id = contactService.save(contactSave)
        redirect(action: 'show', id: id)
    }
}
```
