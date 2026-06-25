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

import android.annotation.SuppressLint
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import org.lsposed.hiddenapibypass.HiddenApiBypass
import pers.ketikai.lsp.mywebview.help.ContextImplHelper
import pers.ketikai.lsp.mywebview.help.WebViewProviderInfoHelper
import pers.ketikai.lsp.mywebview.logging.Logger

@SuppressLint("PrivateApi")
internal class SystemImplHooker(
    override val module: XposedModule,
    override val logger: Logger,
) : Hooker {
    override fun onSystemServerStarting(param: XposedModuleInterface.SystemServerStartingParam) {
        super.onSystemServerStarting(param)
        val classLoader = param.classLoader

        val systemImplClass =
            Class.forName("com.android.server.webkit.SystemImpl", false, classLoader)
        val webViewProviderInfoHelper =
            WebViewProviderInfoHelper(classLoader, logger, ContextImplHelper(classLoader, module, logger))

        val getWebViewPackagesMethod = HiddenApiBypass.getDeclaredMethod(systemImplClass, "getWebViewPackages")
        module
            .hook(getWebViewPackagesMethod)
            .intercept { chain ->
                val result = chain.proceed()
                return@intercept webViewProviderInfoHelper.findWebViewProviderInfoArray() ?: result
            }
    }
}
