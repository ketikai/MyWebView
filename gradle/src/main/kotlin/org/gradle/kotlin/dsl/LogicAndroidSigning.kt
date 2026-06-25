package org.gradle.kotlin.dsl

import com.android.build.api.dsl.ApkSigningConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import java.io.File
import java.util.Properties
import kotlin.apply

class LogicAndroidSigning<T: ApkSigningConfig>(private val signings: NamedDomainObjectContainer<T>, private val name: String) {

    fun environment(): NamedDomainObjectProvider<T>? {
        val standardName = name.uppercase()
        val sPath = System.getenv("${standardName}_STORE_FILE_PATH") ?: return null
        val sPassword = System.getenv("${standardName}_STORE_PASSWORD") ?: return null
        val kAlias = System.getenv("${standardName}_KEY_ALIAS") ?: return null
        val kPassword = System.getenv("${standardName}_KEY_PASSWORD") ?: return null

        return signings.register(name) {
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
            storeFile = File(sPath)
            storePassword = sPassword
            keyAlias = kAlias
            keyPassword = kPassword
        }
    }

    fun properties(properties: File): NamedDomainObjectProvider<T>? {
        !properties.exists() && return null
        val props = Properties().apply {
            properties.inputStream().use {
                load(it)
            }
        }

        val standardName = name.uppercase()
        val sPath = props["${standardName}_STORE_FILE_PATH"] as? String ?: return null
        val sPassword = props["${standardName}_STORE_PASSWORD"] as? String ?: return null
        val kAlias = props["${standardName}_KEY_ALIAS"] as? String ?: return null
        val kPassword = props["${standardName}_KEY_PASSWORD"] as? String ?: return null

        return signings.register(name) {
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
            storeFile = File(sPath)
            storePassword = sPassword
            keyAlias = kAlias
            keyPassword = kPassword
        }
    }
}