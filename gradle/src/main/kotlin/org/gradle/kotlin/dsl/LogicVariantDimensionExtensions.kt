package org.gradle.kotlin.dsl

import com.android.build.api.dsl.VariantDimension

val VariantDimension.buildConfigField: LogicBuildConfigField
    get() = LogicBuildConfigField(this)
