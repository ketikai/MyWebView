// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    logic
    alias(libs.plugins.spotless) apply false
}

println("""
    ---
    * NAME: $NAME
    * NAMESPACE: $NAMESPACE
    * COMPILE_SDK: $COMPILE_SDK

    * APPLICATION_ID: $APPLICATION_ID
    * COMMIT_CODE: $COMMIT_CODE
    * VERSION_CODE: $VERSION_CODE
    * VERSION_NAME: $VERSION_NAME
    * MIN_SDK: $MIN_SDK
    * TARGET_SDK: $TARGET_SDK
    ---
""".trimIndent())