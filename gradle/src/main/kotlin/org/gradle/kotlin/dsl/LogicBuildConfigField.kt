package org.gradle.kotlin.dsl

import com.android.build.api.dsl.VariantDimension

class LogicBuildConfigField(val variantDimension: VariantDimension) {

    inline operator fun <reified T: Any> set(name: String, content: T) {
        val value = if (content is String) {
            "\"$content\""
        } else {
            "$content"
        }
        variantDimension.buildConfigField(T::class.java.name, name, value)
    }
}