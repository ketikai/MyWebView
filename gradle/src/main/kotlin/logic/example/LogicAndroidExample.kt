package logic.example

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.detect
import java.io.File

internal class LogicAndroidExample private constructor() {

    fun ApplicationExtension.signing(project: Project) {
        signingConfigs {
            detect {
                environment()
                // 或
                properties(project.file("local.properties"))
            }
        }
    }
}