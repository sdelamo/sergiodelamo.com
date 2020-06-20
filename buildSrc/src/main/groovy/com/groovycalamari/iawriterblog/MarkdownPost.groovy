package com.groovycalamari.iawriterblog

import edu.umd.cs.findbugs.annotations.NonNull
import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class MarkdownPost implements Post {

    public static final String TITLE = "title"
    public static final String DESCRIPTION = "description"
    public static final String DATE = "date"
    String filename

    Map<String, String> metadata

    List<String> lines

    @NonNull
    Type getType() {
        metadata.containsKey(DATE) ? Type.POST : Type.PAGE
    }

    String getTitle() {
        metadata.get(TITLE)
    }

    @Override
    String getDate() {
        return metadata.get(DATE)
    }

    @Override
    String getContent() {
        lines.join("\n")
    }

    @Override
    String getDescription() {
        metadata.get(DESCRIPTION)
    }

    @Override
    String getPath() {
        return filename.replaceAll(".md", ".html")
    }
}
