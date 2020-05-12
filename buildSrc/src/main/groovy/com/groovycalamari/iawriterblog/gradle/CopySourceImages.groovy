package com.groovycalamari.iawriterblog.gradle


import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class CopySourceImages extends DefaultTask {
    File sourceDir

    File outputDir

    @InputDirectory
    File getSourceDir() {
        return this.sourceDir
    }

    @OutputDirectory
    File getOutputDir() {
        return this.outputDir
    }

    CopySourceImages() {
        File archive = project.file('archive')
        if (archive.exists()) {
            sourceDir = archive
        }
        outputDir = project.buildDir
    }

    @TaskAction
    void copyCss() {
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(sourceDir)
                copySpec.into(outputDir)
                copySpec.include(IAWriterBlogPlugin.IMAGE_EXTENSIONS)
            }
        })
    }
}
