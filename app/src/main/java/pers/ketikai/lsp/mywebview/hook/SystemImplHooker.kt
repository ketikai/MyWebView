package pers.ketikai.lsp.mywebview.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import pers.ketikai.lsp.mywebview.logging.Logger
import pers.ketikai.lsp.mywebview.help.SystemServerHelper
import pers.ketikai.lsp.mywebview.help.WebViewProviderInfoHelper

internal class SystemImplHooker: Hooker, IXposedHookLoadPackage {
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        lpparam.packageName != "android" && return
        val classLoader = lpparam.classLoader
        val systemImplClass =
            XposedHelpers.findClass("com.android.server.webkit.SystemImpl", classLoader)
        val webViewProviderInfoHelper =
            WebViewProviderInfoHelper(classLoader, SystemServerHelper(classLoader))
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
            })
    }
}