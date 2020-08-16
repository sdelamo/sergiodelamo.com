title: Grails Programmer : How to install new Relic in a Grails 3 app?
date: Feb 20, 2016 17:40
---

# [%title]

[%date]

Tags: #grails

Recently I wanted to install [new relic](http://newrelic.com/) in a Grails 3 app which I distribute through Elastic Beanstalk.

One of the steps described in the new [relic documentation](https://docs.newrelic.com/docs/agents/java-agent/frameworks/aws-elastic-beanstalk-installation-java) is:

> In your WAR file, add the newrelic.jar and newrelic.yml files to WEB-INF/lib/.

How to do that in a Grails 3 application?

Create a folder in the root folder of your application called newrelic
Copy both files (newrelic.jar and newrelic.yml) to the previously created newrelic folder.
Add the next snippet to your build.gradle file:

```groovy
war {
    from('newrelic') {
        into 'WEB-INF/lib'
        include 'newrelic.*'
    }
}
```

Additionally you would probably want to add to the dependencies block in build.gradle the next line:

```groovy
compile "org.grails.plugins:newrelic:3.19.2"
```

This will install the Grails newrelic Plugin to your grails app.

Among other things the plugin will insert an interceptor which names transactions based on your controller/action information.

`NewRelicInterceptor` - An interceptor matching all requests to automatically name transactions as `{controllerName}/{actionName}`.

We are done in the grails side, complete the installation guide and you will have new relic configured.

