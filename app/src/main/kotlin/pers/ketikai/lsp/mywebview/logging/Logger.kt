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
import io.github.libxposed.api.XposedModule
import pers.ketikai.lsp.mywebview.app.BuildConfig

@Suppress("unused")
class Logger(
    private val module: XposedModule,
) {
    val tag: String = BuildConfig.NAME

    fun verbose(any: Any?) {
        module.log(Log.VERBOSE, tag, "$any")
    }

    fun verbose(message: String) {
        module.log(Log.VERBOSE, tag, message)
    }

    fun verbose(
        message: String,
        vararg arguments: Any?,
    ) {
        module.log(Log.VERBOSE, tag, message.format(*arguments))
    }

    fun verbose(throwable: Throwable) {
        val message = throwable.message ?: ""
        module.log(Log.VERBOSE, tag, message, throwable)
    }

    fun debug(any: Any?) {
        module.log(Log.DEBUG, tag, "$any")
    }

    fun debug(message: String) {
        module.log(Log.DEBUG, tag, message)
    }

    fun debug(
        message: String,
        vararg arguments: Any?,
    ) {
        module.log(Log.DEBUG, tag, message.format(*arguments))
    }

    fun debug(throwable: Throwable) {
        val message = throwable.message ?: ""
        module.log(Log.DEBUG, tag, message, throwable)
    }

    fun info(any: Any?) {
        module.log(Log.INFO, tag, "$any")
    }

    fun info(message: String) {
        module.log(Log.INFO, tag, message)
    }

    fun info(
        message: String,
        vararg arguments: Any?,
    ) {
        module.log(Log.INFO, tag, message.format(*arguments))
    }

    fun info(throwable: Throwable) {
        val message = throwable.message ?: ""
        module.log(Log.INFO, tag, message, throwable)
    }

    fun warn(any: Any?) {
        module.log(Log.WARN, tag, "$any")
    }

    fun warn(message: String) {
        module.log(Log.WARN, tag, message)
    }

    fun warn(
        message: String,
        vararg arguments: Any?,
    ) {
        module.log(Log.WARN, tag, message.format(*arguments))
    }

    fun warn(throwable: Throwable) {
        val message = throwable.message ?: ""
        module.log(Log.WARN, tag, message, throwable)
    }

    fun error(any: Any?) {
        module.log(Log.ERROR, tag, "$any")
    }

    fun error(message: String) {
        module.log(Log.ERROR, tag, message)
    }

    fun error(
        message: String,
        vararg arguments: Any?,
    ) {
        module.log(Log.ERROR, tag, message.format(*arguments))
    }

    fun error(throwable: Throwable) {
        val message = throwable.message ?: ""
        module.log(Log.ERROR, tag, message, throwable)
    }

    fun log(
        priority: Int,
        any: Any?,
    ) {
        module.log(priority, tag, "$any")
    }

    fun log(
        priority: Int,
        message: String,
    ) {
        module.log(priority, tag, message)
    }

    fun log(
        priority: Int,
        message: String,
        vararg arguments: Any?,
    ) {
        module.log(priority, tag, message.format(*arguments))
    }

    fun log(
        priority: Int,
        throwable: Throwable,
    ) {
        val message = throwable.message ?: ""
        module.log(priority, tag, message, throwable)
    }
}
