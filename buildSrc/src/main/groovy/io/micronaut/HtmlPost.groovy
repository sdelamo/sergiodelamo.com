package io.micronaut

import groovy.transform.CompileStatic

@CompileStatic
class HtmlPost {
    String path
    PostMetadata metadata
    String html
    Set<String> tags

    String getExternalUrl() {
        metadata["external_url"] ?: metadata["speakerdeck"]
    }

    String getTitle() {
        String postTitle = metadata['title']
        if (isLinkToVideo()) {
            postTitle = "📼 " + postTitle
        }
        if (isLinkToGuide())  {
            postTitle = "📖 " + postTitle
        }
        postTitle
    }

    boolean isLinkToVideo() {
        isVideoUrl(metadata['external_url'] as String) || isVideoUrl(metadata['video'] as String)
    }

    boolean isLinkToGuide() {
        isGuideUrl(metadata['external_url'] as String)
    }

    static boolean isVideoUrl(String url) {
        if (!url) {
            return false
        }
        url.contains("youtube")
    }

    static boolean isGuideUrl(String url) {
        if (!url) {
            return false
        }
        url.startsWith("https://guides.micronaut.io")
    }

    String getLink() {
        "${metadata['url']}/blog/${path}"
    }
}

