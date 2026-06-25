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

package pers.ketikai.lsp.mywebview.help

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import io.github.libxposed.api.XposedModule
import org.lsposed.hiddenapibypass.HiddenApiBypass
import pers.ketikai.lsp.mywebview.logging.Logger

@SuppressLint("PrivateApi")
internal class ContextImplHelper(
    classLoader: ClassLoader,
    module: XposedModule,
    private val logger: Logger,
) : PackageManagerProvider {
    @Volatile
    override var packageManager: PackageManager? = null
        private set

    init {
        val contextImplClass =
            Class.forName("android.app.ContextImpl", false, classLoader)

        val getPackageManagerMethod =
            HiddenApiBypass.getDeclaredMethod(contextImplClass, "getPackageManager")

        @Suppress("UnnecessaryVariable")
        module
            .hook(getPackageManagerMethod)
            .intercept { chain ->
                val result = chain.proceed()
                packageManager != null && return@intercept result
                logger.info("Get a package manager ...")
                val systemServerPackageManager = result
                logger.info(systemServerPackageManager)
                systemServerPackageManager == null && return@intercept result
                packageManager = systemServerPackageManager as PackageManager?
                return@intercept result
            }
    }
}
