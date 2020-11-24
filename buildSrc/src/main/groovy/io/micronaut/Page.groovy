package io.micronaut

import groovy.transform.CompileStatic

@CompileStatic
class Page implements Content {
    String filename
    Map<String, String> metadata
    String content

    @Override
    String getPath() {
        filename
    }

    @Override
    String getTitle() {
        metadata && metadata.containsKey('title') ? metadata['title'] : null
    }

    @Override
    String getBody() {
        metadata && metadata.containsKey('body') ? metadata['body'] : null
    }

    @Override
    String getDatePublished() {
        metadata && metadata.containsKey('date_published') ? metadata['date_published'] : null
    }

    @Override
    String getSummary() {
        metadata && metadata.containsKey('summary') ? metadata['summary'] : null
    }

    @Override
    List<String> getKeywords() {
        metadata && metadata.containsKey('keywords') ? metadata['keywords'].split(",") as List<String> : null
    }

    @Override
    String getRobots() {
        metadata && metadata.containsKey('robots') ? metadata['robots'] : null
    }
}
