/*
 *    MyWebView
 *    Copyright (C) 2025  ketikai
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    fun info(
        message: String,
        vararg arguments: Any?,
    ) {
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

    fun debug(
        message: String,
        vararg arguments: Any?,
    ) {
        Log.d(ID, message.format(*arguments))
    }

    fun debug(throwable: Throwable) {
        val message = throwable.message ?: ""
        Log.d(ID, message, throwable)
    }
}
