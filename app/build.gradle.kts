import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.HasBuiltinDelimiterForLicense
import com.diffplug.spotless.LineEnding
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.spotless)
}

android {
    namespace = "pers.ketikai.lsp.mywebview"
    compileSdk = 36

    defaultConfig {
        applicationId = "pers.ketikai.lsp.mywebview"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildOutputs {
        all {
            this as BaseVariantOutputImpl
            outputFileName = "${rootProject.name}-${defaultConfig.versionName}-$name.apk"
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(project.fileTree("libraries"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.preference)

    compileOnly(libs.xposed.api)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

spotless {
    fun applyLicenseHeader(it: HasBuiltinDelimiterForLicense) {
        rootProject.file("HEADER.txt").run {
            if (exists()) {
                it.licenseHeaderFile(this)
            }
        }
    }

    fun applyKtlint(it: BaseKotlinExtension.KtlintConfig) {
        var config = project.file(".editorconfig")
        if (config.exists()) {
            it.setEditorConfigPath(config)
        } else {
            config = project.rootProject.file(".editorconfig")
            if (config.exists()) {
                it.setEditorConfigPath(config)
            }
        }
    }

    encoding(Charsets.UTF_8)
    lineEndings = LineEnding.GIT_ATTRIBUTES_FAST_ALLSAME

    kotlinGradle {
        target("*.gradle.kts")
        applyKtlint(ktlint())
        endWithNewline()
    }

    kotlin {
        target("src/*/java/**/*.kt", "src/*/java/**/*.kts", "src/*/kotlin/**/*.kt", "src/*/kotlin/**/*.kts")
        applyLicenseHeader(this)
        applyKtlint(ktlint())
        endWithNewline()
    }
}
