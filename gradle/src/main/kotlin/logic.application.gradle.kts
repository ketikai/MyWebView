import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import logic.LogicAndroid

extensions.configure<ApplicationExtension>("android") {
    LogicAndroid.configureCommonExtensionBefore(project, this)

    defaultConfig {
        applicationId = APPLICATION_ID
        targetSdk = TARGET_SDK
        versionCode = VERSION_CODE
        versionName = VERSION_NAME
    }

    LogicAndroid.configureCommonExtensionAfter(project, this)
}

extensions.configure<ApplicationAndroidComponentsExtension>("androidComponents") {
    onVariants(selector().all()) {
        LogicAndroid.registerCleaner(project, it.name)
    }
}
