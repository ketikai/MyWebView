import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import logic.LogicAndroid

pluginManager.withPlugin("com.android.library") {
    extensions.configure<LibraryExtension>("android") {
        LogicAndroid.configureCommonExtensionBefore(project, this)

        defaultConfig {
        }
        buildTypes.named("release") {
            consumerProguardFiles("consumer-rules.pro")
        }

        LogicAndroid.configureCommonExtensionAfter(project, this)
    }

    extensions.configure<LibraryAndroidComponentsExtension>("androidComponents") {
        onVariants(selector().all()) {
            LogicAndroid.registerCleaner(project, it.name)
        }
    }
}
