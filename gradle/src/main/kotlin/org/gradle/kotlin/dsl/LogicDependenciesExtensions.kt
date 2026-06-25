@file:Suppress("UnusedReceiverParameter", "unused")

package org.gradle.kotlin.dsl

import logic.LogicTasks
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree

fun DependencyHandlerScope.libraries(project: Project): ConfigurableFileTree {
    return project.fileTree("libraries")
}

fun DependencyHandlerScope.jar(base: Project, project: String, task: String = "", basename: String = "", version: String = "", classifier: String = ""): ConfigurableFileCollection {
    if (task.isNotBlank()) {
        LogicTasks.run(base, project, task)
    }
    var filename = basename.ifBlank {
        val i = project.lastIndexOf(':')
        if (i < 0) {
            project
        } else {
            project.substring(i + 1)
        }
    }
    filename = if (version.isBlank()) {
        filename
    } else {
        "$filename-$version"
    }
    filename = if (classifier.isBlank()) {
        filename
    } else {
        "$filename-$classifier"
    }
    filename += ".jar"
    return base.objects.fileCollection().apply {
        from(base.file("${project.removePrefix(":").replace(':', '/')}/build/libs/$filename"))
    }
}
