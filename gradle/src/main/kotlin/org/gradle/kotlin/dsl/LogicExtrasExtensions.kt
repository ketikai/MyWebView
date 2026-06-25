@file:Suppress("unused", "UnusedReceiverParameter")

package org.gradle.kotlin.dsl

import logic.LogicMetadata
import org.gradle.api.Project

val Project.NAME: String
    get() = LogicMetadata.NAME
val Project.NAMESPACE: String
    get() = LogicMetadata.NAMESPACE
val Project.COMPILE_SDK: Int
    get() = LogicMetadata.COMPILE_SDK

val Project.APPLICATION_ID: String
    get() = LogicMetadata.APPLICATION_ID
val Project.COMMIT_CODE: Int
    get() = LogicMetadata.COMMIT_CODE
val Project.VERSION_CODE: Int
    get() = LogicMetadata.VERSION_CODE
val Project.VERSION_NAME: String
    get() = LogicMetadata.VERSION_NAME
val Project.MIN_SDK: Int
    get() = LogicMetadata.MIN_SDK
val Project.TARGET_SDK: Int
    get() = LogicMetadata.TARGET_SDK
