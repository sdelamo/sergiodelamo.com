---
title: Extra Grails Naming Conventions
summary: The Grails framework is a covention over configuration framework. Through the years I have created extra naming conventions.
date_published: 2021-09-22T12:01:10+01:00
keywords:grails,gorm
---

# [%title]

The [Grails](https://grails.org) framework is a covention over configuration framework. Through the years, I have created extra naming conventions.

## Exisiting naming conventions

There are several conventions around naming already: 

- `Controller` is the suffix for Controllers. For example: `BookController.groovy`. They are in the folder `grails-app/controllers`.
- `Service` is the suffix for services. For example: `BookInventoryService`. They are in the folder `grails-app/services`.
- `TagLib` is the suffix for tag libraries. For example: `BootstrapTagLib`. They are in the folder `grails-app/taglib`
- `UrlMappings` is the suffix for the [URL mappings](http://docs.grails.org/latest/guide/theWebLayer.html#urlmappings). You can have multiple files ending with `UrlMappings` and they are in the folder `grails-app/controllers`.
- `Spec` for Spock specifications. They are in the folders `src/test/groovy` or `src/integrationTest/groovy`

## Extra naming conventions suffixes

I use these suffixes:

- `Entity`
- `GormService`
- `Listener`
- `ListenerService`
- `JobService`
- `Utils`
- `Client`
- `GebSpec`
- `Page`
- `Module`
- `Create`
- `Save`
- `Update`

### `Entity` suffix for Domain classes

- Domain classes are in the folder `grails-app/domain`. I like to suffix them with `Entity` and remove the `Entity` part from the table name using the [`mapping` static property](https://docs.grails.org/latest/ref/Domain%20Classes/mapping.html)

```groovy
class BookEntity {

    String title
    String about
    static mapping = {
        about type: 'text'
        table 'book'
    }
}
```

###  `GormService` suffix for GORM related services

I encapsulate [GORM](https://gorm.grails.org) logic in a few services in the `grails-app/services` folder. I like to suffix them with `GormService`. I have used `DataService` in the past but lately I have settled with `GormService`. E.g. 

```groovy
@Service(BookEntity)
interface BookService {
    BookEntity save(String title, String about)
}
```

### Suffix `Listener` for Kakfa, RabbmitMQ listeners

If I integrate a Grails application with [Micronaut Kafka](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/) or [Micronaut RabbitMQ](https://micronaut-projects.github.io/micronaut-rabbitmq/latest/guide/) I use the `Listener` suffix for those classes. 

```groovy
@KafkaListener
class AnalyticsListener {

    @Inject
    BookAnalyticsGormService bookAnalyticsGormService

    @Topic('analytics')
    void updateAnalytics(Map payload) {
        ...
    }
}
```

These listeners are in the folder `src/main/groovy`

### Suffix `Listener` for classes subscribing to events 

I suffix with `ListenerService` services containing methods annotated with `@Subscriber`. 


 ```groovy
class BookSavedListenerService {
     @Transactional
     @Subscriber 
     void saveBook(Book book) {
     ...
```

Also, classes extending `AbstractPersistenceEventListener` such as the `UserPasswordEncoderListener` which you get when you execute `s2-quickstart` with [Grails Spring Security Core Plugin](https://grails-plugins.github.io/grails-spring-security-core/)

I use the suffix `ListenerService` for classes in `grails-app/services`. 

I use the suffix `Listener` for classes in `src/main/groovy`.

### Suffix `JobService` for Jobs

I use the suffix `JobService` for classes in `grails-app/services` with methods annotated with `org.springframework.scheduling.annotation.Scheduled`:

```groovy
@Slf4j
@CompileStatic
class DailyEmailJobService  {

    static lazyInit = false 

    EmailService emailService 

    @Scheduled(cron = "0 30 4 1/1 * ?") 
    void execute() {
    ...
}
```

### Suffix `Utils` for utility classes.

I use the suffix `Utils` for classes in `grails-app/utils`. For example `EAN13Utils.groovy`. `Utils` classe are mostly final classes with static methods.


### Sufix `Client` for HTTP Clients.

I use the suffix `Client` for declarative Micronaut HTTP Client in `src/main/groovy`.


```groovy
package example.grails

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client(id = "itunes") 
interface ItunesClient {

    @Get("/search?limit=25&media=music&entity=album&term={term}") 
    SearchResult search(String term)
```

### Suffix `GebSpec` for Geb integration tests

I use the suffix `GebSpec` for Grails Integration tests which use [Geb](https://gebish.org). They are in the folder `src/integrationTest/groovy`. For example: 

```groovy
@Integration
class ContactPageGebSpec extends GebSpec {

    void "go to contacts index page"() {
        when:
        to(ContactPage)

        then:
        at(ContactPage)
}
```

### Suffix `Page` for Geb Pages

I use the suffix `Page` for [Geb Page Objects](https://gebish.org/manual/current/#scripting-with-page-objects). They are in the folder `src/integrationTest/groovy`. For example: 

```groovy
class ContactPage extends Page {
    static url = '/'
    static at = {
        $('div#container-contact')
    }

    static content = {
        navbar { $('#contact-navbar').module(AddContactModule) }
    }

    void addContact() {
        navbar.add()
    }
}
```

### Suffix `Module` for Geb Modules

I use the suffix `Module` for [Geb Modules](https://gebish.org/manual/current/#modules). They are in the folder `src/integrationTest/groovy`. For example: 


```groovy
import geb.Module

class AddContactModule extends Module {
    static content = {
        addContactLink(to: AddContactPage) { $('a', text: contains('Add Contact')) }
    }

    void add() {
        addContactLink.click()
    }
}
```

### Suffix `Create`, `Update` and `Save` for POGOs used in the create, update and save actions

I like to use a different POGO for the create action and the save action. 

The `Create` POGO normally allows anything to be null and it is the object that the controller passes to the view (e.g. the GSP) as the model. 

```groovy
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class ContactCreate implements Validateable {
    String name
    String nameInvalid

    static constraints = {
        name nullable: true
        nameInvalid nullable: true
    }
}
```

The `Save` POGO contains the constraints which are validated before passing the object into the service layer:

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

This is an example of how I use them in a controller:

```groovy
class ContactController {
    ...
    def create() {
        [contact: new ContactCreate()]
    }

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

## Don't make me think

I became aware of the Book [Don't make me think](https://www.amazon.com/Dont-Make-Me-Think-Usability/dp/0321344758) while attending a attending a usability class at the unversity. It had a big impact on me when I read it. I always felt the same about Grails naming conventions, they don't make you think and they keep me productive.

