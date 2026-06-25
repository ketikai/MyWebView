package logic

import com.android.build.api.dsl.CommonExtension
import logic.LogicMetadata.COMPILE_SDK
import logic.LogicMetadata.NAMESPACE
import logic.LogicMetadata.MIN_SDK
import logic.LogicMetadata.TARGET_SDK
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import java.util.Locale

internal object LogicAndroid {

    fun configureCommonExtensionBefore(project: Project, extension: CommonExtension) {
        extension.apply {
            namespace = "$NAMESPACE.${project.name.replace('-', '.')}"
            compileSdk = COMPILE_SDK

            enableKotlin = true

            buildFeatures.apply {
                buildConfig = true
            }

            defaultConfig.apply {
                minSdk = MIN_SDK
            }

//            val javaVersion = JavaVersion.VERSION_1_8
//            compileOptions.apply {
//                sourceCompatibility = javaVersion
//                targetCompatibility = javaVersion
//            }

            buildTypes.named("debug").configure {
                isMinifyEnabled = false
            }

            buildTypes.named("release").configure {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
    }

    fun configureCommonExtensionAfter(project: Project, extension: CommonExtension) {
        extension.apply {
            lint.apply {
                targetSdk = TARGET_SDK
            }
        }
    }

    fun registerCleaner(project: Project, variantName: String) {
        variantName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        val taskName = "remove${variantName}StubClasses"
        project.tasks.register(taskName, Delete::class.java) {
            delete("${project.layout.buildDirectory}/intermediates/classes/$variantName/android")
        }
        project.tasks.whenTaskAdded {
            if (name == "process${variantName}JavaRes") {
                dependsOn(taskName)
            }
        }
    }
}