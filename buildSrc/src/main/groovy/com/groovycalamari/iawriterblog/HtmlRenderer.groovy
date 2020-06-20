package com.groovycalamari.iawriterblog

import javax.annotation.Nonnull
import javax.validation.Valid
import javax.validation.constraints.NotNull

interface HtmlRenderer {

    @Nonnull
    String render(@Nonnull @NotNull @Valid MarkdownPost post)
}
