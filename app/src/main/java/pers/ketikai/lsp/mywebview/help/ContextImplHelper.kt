package pers.ketikai.lsp.mywebview.help

import android.content.pm.PackageManager
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import pers.ketikai.lsp.mywebview.logging.Logger

internal class ContextImplHelper(classLoader: ClassLoader): PackageManagerProvider {
    @Volatile
    override var packageManager: PackageManager? = null
        private set
    
    init {
        val contextImplClass =
            XposedHelpers.findClass("android.app.ContextImpl", classLoader)

        XposedHelpers.findAndHookMethod(
            contextImplClass, "getPackageManager",
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
            }
        )
    }
}