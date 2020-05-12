package com.groovycalamari.iawriterblog.gradle

import groovy.transform.CompileStatic
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

@CompileStatic
class SiteExtension {

    final Property<String> title
    final Property<String> url
    final Property<String> about
    final Property<File> iatemplate
    final Property<File> sourceDir
    final Property<File> outputDir
    final ListProperty<String> keywords
    final Property<String> robots
    final Property<String> robotsArchive
    final Property<String> robotsTags

    @Inject
    SiteExtension(ObjectFactory objects) {
        title = objects.property(String)
        url = objects.property(String)
        about = objects.property(String)
        iatemplate = objects.property(File)
        sourceDir = objects.property(File)
        outputDir = objects.property(File)
        keywords = objects.listProperty(String)
        robots = objects.property(String)
        robotsArchive = objects.property(String)
        robotsTags = objects.property(String)
    }
}
