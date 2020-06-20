package com.groovycalamari.iawriterblog

import edu.umd.cs.findbugs.annotations.NonNull
import edu.umd.cs.findbugs.annotations.Nullable

interface Post {

    @NonNull
    Map<String, String> getMetadata()

    @Nullable
    String getTitle()

    @Nullable
    String getDate()

    @Nullable
    String getContent()

    @Nullable
    String getDescription()

    @NonNull
    String getPath();

    @NonNull
    Type getType()
}
