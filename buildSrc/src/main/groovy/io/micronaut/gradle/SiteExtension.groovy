package io.micronaut.gradle

import groovy.transform.CompileStatic
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

import javax.inject.Inject

@CompileStatic
class SiteExtension {

    final Property<File> pages

    final Property<File> posts

    final Property<File> template

    final Property<File> assets

    final Property<File> output

    final Property<String> title

    final Property<String> url

    final Property<String> summary

    final ListProperty<String> keywords

    final Property<String> robots

    @Inject
    SiteExtension(ObjectFactory objects) {
        assets = objects.property(File)
        pages = objects.property(File)
        posts = objects.property(File)
        template = objects.property(File)
        output = objects.property(File)
        title = objects.property(String)
        url = objects.property(String)
        summary = objects.property(String)
        keywords = objects.listProperty(String)
        robots = objects.property(String)
    }
}
