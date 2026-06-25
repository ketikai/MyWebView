package logic

import org.gradle.api.Project
import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProgressEvent
import org.gradle.tooling.ProgressListener
import org.gradle.tooling.ResultHandler

internal object LogicTasks {

    fun run(base: Project, project: String, task: String = ":assemble") {
        var path = project
        if (path.startsWith(':')) {
            path = path.removePrefix(":")
        }
        path = path.replace(':', '/')
        println("> Connecting project $project")
        GradleConnector.newConnector().forProjectDirectory(base.file(path)).connect().use {
            println("> Connected project $project")
            val launcher = it.newBuild().forTasks(task)
                .setStandardError(System.err)
                .setColorOutput(true)
            launcher.addProgressListener(object : ProgressListener {
                @Volatile
                private var lastDescription: String? = null
                override fun statusChanged(event: ProgressEvent) {
                    val description = event.description
                    if (description == lastDescription) {
                        return
                    }
                    if (description.startsWith("Task :")) {
                        lastDescription = description
                        println("> $description")
                    }
                }
            })
            launcher.run(object : ResultHandler<Void> {
                override fun onComplete(result: Void?) {}

                override fun onFailure(failure: GradleConnectionException) {
                    error(failure)
                }
            })
        }
    }
}