package io.micronaut

import io.micronaut.core.annotation.Nullable

interface PostMetadata {

    String get(String name)

    @Nullable
    String getUrl()

    @Nullable
    String getTitle()

    @Nullable
    String getAuthor()

    @Nullable
    String getDatePublished()

    Map<String, String> toMap()
}