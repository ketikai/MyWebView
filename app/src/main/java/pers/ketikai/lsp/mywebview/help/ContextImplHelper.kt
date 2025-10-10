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

import android.content.pm.PackageManager
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import pers.ketikai.lsp.mywebview.logging.Logger

internal class ContextImplHelper(
    classLoader: ClassLoader,
) : PackageManagerProvider {
    @Volatile
    override var packageManager: PackageManager? = null
        private set

    init {
        val contextImplClass =
            XposedHelpers.findClass("android.app.ContextImpl", classLoader)

        XposedHelpers.findAndHookMethod(
            contextImplClass,
            "getPackageManager",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    try {
                        super.afterHookedMethod(param)
                        param.hasThrowable() && return
                        packageManager != null && return
                        Logger.info("Get a package manager ...")
                        val systemServerPackageManager = param.result
                        Logger.info(systemServerPackageManager)
                        systemServerPackageManager == null && return
                        packageManager = systemServerPackageManager as PackageManager?
                        Logger.info("Got it!")
                    } catch (e: Throwable) {
                        Logger.info(e)
                    }
                }
            },
        )
    }
}
