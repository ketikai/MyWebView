import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.HasBuiltinDelimiterForLicense
import com.diffplug.spotless.LineEnding


plugins {
    logic
    application
    alias(libs.plugins.spotless)
}

base.archivesName.set("$NAME-v$VERSION_NAME-$VERSION_CODE")

android {
    defaultConfig {
        buildConfigField["NAME"] = NAME
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    signingConfigs {
        detect {
            properties(project.file("local.properties"))
        }
    }

    buildTypes {
        release {
            sign(this)
        }
    }
}

dependencies {
    compileOnly(libs.libxposed.api)
    implementation(libs.hidden.api.bypass)
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
