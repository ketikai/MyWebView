package pers.ketikai.lsp.mywebview.logging

import android.util.Log
import de.robv.android.xposed.XposedBridge
import pers.ketikai.lsp.mywebview.MyWebView
import java.io.PrintWriter
import java.io.StringWriter

@Suppress("unused")
internal object Logger {

    val ID: String = MyWebView::class.java.name

    fun info(any: Any?) {
        Log.i(ID, "$any")
        XposedBridge.log("$ID: $any")
    }

    fun info(message: String) {
        Log.i(ID, message)
        XposedBridge.log("$ID: $message")
    }

    fun info(message: String, vararg arguments: Any?) {
        Log.i(ID, message.format(*arguments))
        XposedBridge.log("$ID: ${message.format(*arguments)}")
    }

    fun info(throwable: Throwable) {
        val message = throwable.message ?: ""
        Log.i(ID, message, throwable)
        val stackTrace = StringWriter()
        PrintWriter(stackTrace).use {
            throwable.printStackTrace(it)
        }
        XposedBridge.log("$ID: $message\n$stackTrace")
    }

    fun debug(any: Any?) {
        Log.d(ID, "$any")
    }

    fun debug(message: String) {
        Log.d(ID, message)
    }

    fun debug(message: String, vararg arguments: Any?) {
        Log.d(ID, message.format(*arguments))
    }

    fun debug(throwable: Throwable) {
        val message = throwable.message ?: ""
        Log.d(ID, message, throwable)
    }
}