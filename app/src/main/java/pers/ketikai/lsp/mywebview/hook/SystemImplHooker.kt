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

package pers.ketikai.lsp.mywebview.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import pers.ketikai.lsp.mywebview.help.ContextImplHelper
import pers.ketikai.lsp.mywebview.help.WebViewProviderInfoHelper
import pers.ketikai.lsp.mywebview.logging.Logger

internal class SystemImplHooker :
    Hooker,
    IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        lpparam.packageName != "android" && return
        val classLoader = lpparam.classLoader
        val systemImplClass =
            XposedHelpers.findClass("com.android.server.webkit.SystemImpl", classLoader)
        val webViewProviderInfoHelper =
            WebViewProviderInfoHelper(classLoader, ContextImplHelper(classLoader))
        XposedHelpers.findAndHookMethod(
            systemImplClass,
            "getWebViewPackages",
            object : XC_MethodHook() {
                @Suppress("UNCHECKED_CAST")
                override fun afterHookedMethod(param: MethodHookParam) {
                    try {
                        super.afterHookedMethod(param)
                        param.hasThrowable() && return
                        param.result =
                            webViewProviderInfoHelper.findWebViewProviderInfoArray() ?: return
                    } catch (throwable: Throwable) {
                        Logger.info(throwable)
                    }
                }
            },
        )
    }
}
