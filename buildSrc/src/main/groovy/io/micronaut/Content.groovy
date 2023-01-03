package io.micronaut

import io.micronaut.core.annotation.Nullable


interface Content {
    @Nullable String getPath();
    @Nullable String getTitle();
    /**
     *
     * @return Body class
     */
    @Nullable String getBody();
    @Nullable String getDatePublished();
    @Nullable String getSummary();
    @Nullable List<String> getKeywords();
    @Nullable String getRobots();
}
