pluginManagement {
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
        maven { url = uri("https://jitpack.io") }

        mavenCentral()
        google()
    }

    versionCatalogs {
        register("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

rootProject.name = "logic"
