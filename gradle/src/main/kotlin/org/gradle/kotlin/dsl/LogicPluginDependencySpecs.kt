package org.gradle.kotlin.dsl

import org.gradle.plugin.use.PluginDependencySpec

class LogicPluginDependencySpecs(vararg val origin: PluginDependencySpec) {

    infix fun apply(
        apply: Boolean
    ): LogicPluginDependencySpecs {
        for (spec in origin) {
            spec.apply(apply)
        }
        return this
    }
}