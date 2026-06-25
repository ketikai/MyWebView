package logic

import kotlin.properties.Delegates

internal object LogicMetadata {
    const val NAME = "MyWebView"

    const val NAMESPACE = "pers.ketikai.lsp.mywebview"

    const val COMPILE_SDK = 36

    const val APPLICATION_ID = "pers.ketikai.lsp.mywebview"

    var COMMIT_CODE by Delegates.notNull<Int>()
        internal set

    val VERSION_CODE: Int
        get() = COMMIT_CODE

    lateinit var VERSION_NAME: String
        internal set

    const val MIN_SDK  = 30

    const val TARGET_SDK = 36
}