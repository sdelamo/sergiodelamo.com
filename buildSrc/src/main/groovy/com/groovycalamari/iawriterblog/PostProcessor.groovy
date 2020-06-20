package com.groovycalamari.iawriterblog


import groovy.transform.CompileStatic

@CompileStatic
class PostProcessor {

    static final String COLON = ":"
    static final String SEPARATOR = "---"

    List<Post> run(File directory) {

        List<Post> posts = []

        directory.eachFile { file ->
            if (file.path.endsWith(".md") || file.path.endsWith(".markdown")) {
                String filename = file.name
                String line = null
                List<String> lines = []
                int lineNumber = 0
                Map<String, String> metadata = [:]
                boolean metadataProcessed = false
                file.withReader { reader ->
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(SEPARATOR)) {
                            metadataProcessed = true
                            continue
                        }
                        if (!metadataProcessed && line.contains(COLON)) {
                            String metadataKey = line.substring(0, line.indexOf(COLON as String)).trim()
                            String metadataValue = line.substring(line.indexOf(COLON as String) + COLON.length()).trim()
                            metadata[metadataKey] = metadataValue
                        }
                        line = replaceLineWithMetadata(line, metadata)
                        if (metadataProcessed) {
                            lines << line
                        }

                        lineNumber++
                    }
                }
                posts << new MarkdownPost(filename: filename, lines: lines, metadata: metadata)
            }
        }
        posts
    }

    static String replaceLineWithMetadata(String line, Map<String, String> metadata) {
        for (String metadataKey : metadata.keySet()) {
            if (line.contains("[%${metadataKey}]".toString())) {
                String value = metadata[metadataKey]
                line = line.replaceAll("\\[%${metadataKey}\\]".toString(), value)
            }
        }
        line.replaceAll('\\$', '&#36;')
    }
}
