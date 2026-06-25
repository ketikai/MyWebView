package logic.tasks

import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import java.io.File
import kotlin.system.exitProcess

@Suppress("unused")
abstract class AspectJCompiledJar: Jar() {

    @Internal
    val aspectj: Property<Configuration> = project.objects.property(Configuration::class.java).apply {
        set(project.configurations.named("aspectj"))
    }

    @Internal
    val main: Property<String> = project.objects.property(String::class.java).apply {
        set("org.aspectj.tools.ajc.Main")
    }

    @Internal
    val aspect: Property<Configuration> = project.objects.property(Configuration::class.java).apply {
        set(project.configurations.named("aspect"))
    }

    private val aspects: ListProperty<TaskProvider<*>> = project.objects.listProperty(TaskProvider::class.java).apply {
        set(mutableListOf())
    }

    private val input: ListProperty<TaskProvider<*>> = project.objects.listProperty(TaskProvider::class.java).apply {
        set(mutableListOf())
    }

    @Internal
    val args: ListProperty<String> = project.objects.listProperty(String::class.java).apply {
        set(mutableListOf())
    }

    @Internal
    val failOnError: Property<Boolean> = project.objects.property(Boolean::class.java).apply {
        set(true)
    }

    init {
        group = "build"
    }

    fun aspect(vararg taskProviders: TaskProvider<*>) {
        dependsOn(*taskProviders)
        aspects.addAll(*taskProviders)
    }

    fun input(vararg taskProviders: TaskProvider<*>) {
        dependsOn(*taskProviders)
        input.addAll(*taskProviders)
    }

    @TaskAction
    override fun copy() {
        val aspectj = aspectj.get()
        val main = main.get()
        val java = project.extensions["java"] as JavaPluginExtension
        val aspectJDestinationDir = project.layout.buildDirectory.dir("classes/aspectj").get().asFile
        if (!aspectJDestinationDir.exists()) {
            aspectJDestinationDir.mkdirs()
        }
        val arguments = mutableListOf(
            "-source", java.sourceCompatibility.toString(),
            "-target", java.targetCompatibility.toString(),
            "-classpath", resolveFiles(aspectj),
            "-aspectpath", resolveFiles(resolveOutputFiles(aspects.get()) + aspect.get().files),
            "-inpath", resolveFiles(resolveOutputFiles(input.get())),
            "-d", aspectJDestinationDir.absolutePath
        ) + args.get()

        project.layout.buildDirectory.file("tmp/aspectj/ajc.options").get().asFile.apply {
            if (!exists()) {
                parentFile.apply {
                    if (!exists()) {
                        mkdirs()
                    }
                }
                createNewFile()
            }
            writeText(arguments.joinToString("\n"))
        }

        val output = project.providers.javaexec {
            classpath = aspectj
            mainClass.set(main)
            args = arguments
            isIgnoreExitValue = true
        }
        var message = output.standardOutput.asText.get()
        if (message.isNotBlank()) {
            println(message)
        }
        message = output.standardError.asText.get()
        if (message.isNotBlank()) {
            System.err.println(message)
        }

        val exitValue = output.result.get().exitValue
        if (exitValue != 0) {
            if (failOnError.get()) {
                error("exit code $exitValue")
            }
        }

        from(aspectJDestinationDir)
        destinationDirectory.set(project.layout.buildDirectory.dir("libs"))
        super.copy()
    }

    private fun resolveFiles(files: Iterable<File>): String {
        return files.joinToString(";", transform = File::getAbsolutePath)
    }

    private fun resolveOutputFiles(taskProviders: Iterable<TaskProvider<*>>): MutableSet<File> {
        return taskProviders.flatMap {
            it.get().outputs.files.files
        }.toMutableSet();
    }
}