pluginManagement {
    includeBuild("gradle")
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }

        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }

        mavenCentral()
        google()
    }
}

plugins {
    id("logic")
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":app")
 