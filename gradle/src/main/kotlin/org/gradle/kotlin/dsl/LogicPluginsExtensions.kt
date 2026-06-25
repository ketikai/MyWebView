@file:Suppress("unused")

package org.gradle.kotlin.dsl

import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec

private fun PluginDependenciesSpecScope.internal(pluginId: String): PluginDependencySpec {
    val fullPluginId = "logic.$pluginId"
    println("using plugin: \"$fullPluginId\"")
    return id(fullPluginId)
}

val PluginDependenciesSpecScope.logic: LogicPluginDependencySpecs
    get() {
        return LogicPluginDependencySpecs(
            internal("extras"),
            internal("configurations"),
            internal("tasks"),
        )
    }

val PluginDependenciesSpecScope.java: LogicPluginDependencySpecs
    get() {
        return LogicPluginDependencySpecs(
            id("java"),
            internal("java")
        )
    }

@Suppress("ObjectPropertyName")
val PluginDependenciesSpecScope.`java-library`: LogicPluginDependencySpecs
    get() {
        return LogicPluginDependencySpecs(
            id("java-library"),
            internal("java")
        )
    }

val PluginDependenciesSpecScope.aspectj: LogicPluginDependencySpecs
    get() {
        return LogicPluginDependencySpecs(
            internal("aspectj"),
        )
    }

val PluginDependenciesSpecScope.application: LogicPluginDependencySpecs
    get() {
        return LogicPluginDependencySpecs(
            id("com.android.application"),
            internal("java"),
            internal("application")
        )
    }

val PluginDependenciesSpecScope.parcelize: LogicPluginDependencySpecs
    get() {
        return LogicPluginDependencySpecs(
            id("org.jetbrains.kotlin.plugin.parcelize")
        )
    }

val PluginDependenciesSpecScope.library: LogicPluginDependencySpecs
    get() {
        return LogicPluginDependencySpecs(
            id("com.android.library"),
            internal("java"),
            internal("library")
        )
    }

fun PluginDependenciesSpecScope.include(pluginId: String): PluginDependencySpec {
    return id(pluginId) apply false
}

fun PluginDependenciesSpecScope.include(notation: Provider<PluginDependency>): PluginDependencySpec {
    return alias(notation) apply false
}

fun PluginDependenciesSpecScope.include(pluginSpec: PluginDependencySpec): PluginDependencySpec {
    return pluginSpec apply false
}

fun PluginDependenciesSpecScope.include(pluginSpecs: LogicPluginDependencySpecs): LogicPluginDependencySpecs {
    return pluginSpecs apply false
}
