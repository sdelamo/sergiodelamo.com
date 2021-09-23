---
title: How to use a Groovy trait to output any class as csv?
date_published: 2016-03-15T10:30:00+01:00
keywords:grails
---

# [%title]

The next example shows how to use a Trait to output a CSV string for any Class which implements the trait

```groovy
trait TraitAsCSV {
   List&amp;lt;String&amp;gt; propertyNames() {
        this.metaClass.properties.collect { it.name }.findAll { it != 'class'}
    }

    String csvHeaders() {
        propertyNames().join(delimiter())
    }

    String asCSV() {
        def str = ""
        def arr = []
        for(def propName in propertyNames()) {
            def v = this."$propName"
            arr &amp;lt;&amp;lt;  (v ?: '')
        }
        arr.join(delimiter())
    }

    static String delimiter() {
     ';'
    }
}
class MeetupMember implements TraitAsCSV {
    String name
    String locality
    String twitter
    String facebook
    String tumblr
    String background
    String imageUrl
    String website
}
```

The next Spock test will pass:

```groovy

    def "test trait as csv works"() {

        given:
        def m = new MeetupMember()
        m.name = 'Sergio del Amo'
        m.locality = 'Guadalajara'
        m.twitter = 'https://twitter.com/sdelamo'
        m.facebook = null
        m.tumblr = null
        m.imageUrl = 'http://photos4.meetupstatic.com/photos/member/c/d/6/b/member_254392587.jpeg'
        m.website = 'http://www.meetup.com/es-ES/Warsaw-Groovy-User-Group/members/200767921/'

        when:
        String csvHeaders = m.csvHeaders()

        then:
        csvHeaders == 'imageUrl;locality;twitter;tumblr;facebook;background;name;website'

        when:
        def csv = m.asCSV()

        then:
        csv == "http://photos4.meetupstatic.com/photos/member/c/d/6/b/member_254392587.jpeg;Guadalajara;https://twitter.com/sdelamo;;;;Sergio del Amo;http://www.meetup.com/es-ES/Warsaw-Groovy-User-Group/members/200767921/"
    }
```

This Trait can be used for any class which you wish to output as a comma separated value. Useful to export information to be sued in Excel or to import it into another process.


