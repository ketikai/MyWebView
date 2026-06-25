gradle.taskGraph.whenReady {
    tasks.forEach { task ->
        if (task.name == "mockableAndroidJar") {
            task.enabled = false
        }
    }
}
