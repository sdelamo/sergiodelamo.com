---
title: How to use a Trait to encapsulate Spring Security Core functionality in a Grails 3 App?
date_published: 2016-02-27T09:00:00+01:00
date_modified: 2016-02-27T09:00:00+01:00
---

# [%title]

[%date_published]

```
grails --version
| Grails Version: 3.0.14
| Groovy Version: 2.4.5
| JVM Version: 1.8.0_45
$ grails create-app my app
| Application created at /Users/groovycalamari/Documents/myapp
```

Add one dependency to your build.gradle as shown below to install Spring Security Core Plugin

```groovy
buildscript {
    ext {
        grailsVersion = project.grailsVersion
    }
    repositories {
        mavenLocal()
        maven { url "https://repo.grails.org/grails/core" }
    }
    dependencies {
        classpath "org.grails:grails-gradle-plugin:$grailsVersion"
        classpath 'com.bertramlabs.plugins:asset-pipeline-gradle:2.5.0'
        classpath "org.grails.plugins:hibernate:4.3.10.5"
    }
}

plugins {
    id "io.spring.dependency-management" version "0.5.4.RELEASE"
}

version "0.1"
group "myapp"

apply plugin: "spring-boot"
apply plugin: "war"
apply plugin: "asset-pipeline"
apply plugin: 'eclipse'
apply plugin: 'idea'
apply plugin: "org.grails.grails-web"
apply plugin: "org.grails.grails-gsp"

ext {
    grailsVersion = project.grailsVersion
    gradleWrapperVersion = project.gradleWrapperVersion
}

assets {
    minifyJs = true
    minifyCss = true
}

repositories {
    mavenLocal()
    maven { url "https://repo.grails.org/grails/core" }
}

dependencyManagement {
    imports {
        mavenBom "org.grails:grails-bom:$grailsVersion"
    }
    applyMavenExclusions false
}

dependencies {
    compile "org.springframework.boot:spring-boot-starter-logging"
    compile "org.springframework.boot:spring-boot-starter-actuator"
    compile "org.springframework.boot:spring-boot-autoconfigure"
    compile "org.springframework.boot:spring-boot-starter-tomcat"
    compile "org.grails:grails-dependencies"
    compile "org.grails:grails-web-boot"

    compile "org.grails.plugins:hibernate"
    compile "org.grails.plugins:cache"
    compile "org.hibernate:hibernate-ehcache"
    compile "org.grails.plugins:scaffolding"

    runtime "org.grails.plugins:asset-pipeline"

    testCompile "org.grails:grails-plugin-testing"
    testCompile "org.grails.plugins:geb"

    // Note: It is recommended to update to a more robust driver (Chrome, Firefox etc.)
    testRuntime 'org.seleniumhq.selenium:selenium-htmlunit-driver:2.44.0'
    console "org.grails:grails-console"
    compile 'org.grails.plugins:spring-security-core:3.0.3'
}

task wrapper(type: Wrapper) {
    gradleVersion = gradleWrapperVersion
}
```

Lets create the security-related domain classes:

```
grails s2-quickstart myapp User Role
| Creating User class 'User' and Role class 'Role' in package 'myapp'
| Rendered template Person.groovy.template to destination grails-app/domain/myapp/User.groovy
| Rendered template Authority.groovy.template to destination grails-app/domain/myapp/Role.groovy
| Rendered template PersonAuthority.groovy.template to destination grails-app/domain/myapp/UserRole.groovy
|
************************************************************
* Created security-related domain classes. Your            *
* grails-app/conf/application.groovy has been updated with *
* the class names of the configured domain classes;        *
* please verify that the values are correct.               *
************************************************************
```
Add a default user to grails-app/init/BootStrap.groovy

```groovy
import myapp.*

class BootStrap {

    def springSecurityService

    def init = { servletContext -&gt;

        def userRole = new Role('ROLE_USER').save()

        def me = new User('me@sergiodelamo.com', 'groovycalamari').save()

        UserRole.create me, userRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }

    }

    def destroy = {
    }
}
```

Create a Controller which will return the name of the logged user:

```
grails create-controller WhoAmI
| Created grails-app/controllers/myapp/WhoAmIController.groovy
| Created src/test/groovy/myapp/WhoAmIControllerSpec.groovy
```

The controller code could be something like this:

```groovy

package myapp

import grails.plugin.springsecurity.annotation.Secured

class WhoAmIController {

    def springSecurityService

    @Secured('ROLE_USER')
    def index() {

        render springSecurityService.principal.username
    }
}
```

If we start the app and hit the controller endpoint, after login, we would see the user's email:

Probably you would need to user similar Spring Security Code in almost every controller. Most of the time the domain classes will be associated to a user and you need to know who is logged in. Adding the same code over and over is a bad smell.

If you never used Traits you will probably think about a creating an abstract class and make your controllers inherit from it. Fortunately Groovy Traits offer a much better way to encapsulate this functionality. Lets create a Trait to encapsulate the Spring Security Core Functionality.

```
$ mkdir src/main/groovy/mapp
```

Lets create a file: src/main/groovy/mapp/TraitSCC.groovy

with contents:

```groovy
package myapp

trait TraitSCC {
    def springSecurityService

    def currentUsername() {
        springSecurityService.principal?.username
    }
}
```

Now lets modify the controller to implement this Trait

```groovy
package myapp

import grails.plugin.springsecurity.annotation.Secured

class WhoAmIController implements TraitSCC {

    @Secured('ROLE_USER')
    def index() {
        render currentUsername()
    }
}
```

If you run the app and hit the endpoint you will get the same output as before.

Ok, the Trait use is a great encapsulation of a behaviour but I still need to remember to add the implements TraitSCC code to every controller in oder to access the functionality.

Well we can do better. We can use the @Enhances annotation to make every controller implement a trait.

<!--[code language="groovy" highlight="3,4,6"]-->
```groovy
package myapp

import grails.artefact.Enhances
import org.grails.core.artefact.ControllerArtefactHandler

@Enhances(ControllerArtefactHandler.TYPE)
trait TraitSCC {
        def springSecurityService

    def currentUsername() {
                springSecurityService.principal?.username
    }
}
```

Remove the implements part from the controller:

```groovy
package myapp

import grails.plugin.springsecurity.annotation.Secured

class WhoAmIController {

    @Secured('ROLE_USER')
    def index() {
        render currentUsername()
    }
}
```

If you run the app and hit http://localhost:8080/whoAmI you will get the email back as before.

Note: In previous versions of Grails I had to move the Traits files with the @Enhances annotation to a grails plugin and reference the plugin from the main project. It did not work if you have enhanced classes in the main project.
