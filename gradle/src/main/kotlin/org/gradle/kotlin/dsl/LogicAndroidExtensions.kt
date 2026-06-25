package org.gradle.kotlin.dsl

import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.api.dsl.LibraryExtension
import logic.LogicMetadata
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer

/**
 * 自动探测签名所须的属性并注册
 *
 * `${name.uppercase()}_STORE_FILE_PATH`
 *
 * `${name.uppercase()}_STORE_PASSWORD`
 *
 * `${name.uppercase()}_KEY_ALIAS`
 *
 * `${name.uppercase()}_KEY_PASSWORD`
 *
 * @sample logic.example.LogicAndroidExample.signing
 */
fun <R: ApkSigningConfig, T: NamedDomainObjectContainer<R>> T.detect(name: String = LogicMetadata.NAME, action: Action<LogicAndroidSigning<R>>): T {
    action.invoke(LogicAndroidSigning(this, name))
    return this
}

fun <T: ApplicationExtension> T.sign(buildType: ApplicationBuildType, signing: String = LogicMetadata.NAME): T {
    signingConfigs.findByName(signing)?.let {
        buildType.signingConfig = it
    }
    return this
}

fun <T: LibraryExtension> T.sign(buildType: LibraryBuildType, signing: String = LogicMetadata.NAME): T {
    signingConfigs.findByName(signing)?.let {
        buildType.signingConfig = it
    }
    return this
}
