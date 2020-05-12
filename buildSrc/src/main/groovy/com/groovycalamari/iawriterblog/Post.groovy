package com.groovycalamari.iawriterblog

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class Post {

    public static final String TITLE = "title"
    public static final String DESCRIPTION = "description"
    public static final String DATE = "date"
    String filename

    Map<String, String> metadata

    List<String> lines

    boolean isBlogPost() {
        metadata.containsKey(DATE)
    }

    String getTitle() {
        metadata.get(TITLE)
    }

    String getDescription() {
        metadata.get(DESCRIPTION)
    }


    String getPath() {
        return filename.replaceAll(".md", ".html")
    }
}
