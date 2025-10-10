package pers.ketikai.lsp.mywebview.help

import android.content.pm.PackageManager
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import pers.ketikai.lsp.mywebview.logging.Logger

internal class SystemServerHelper(classLoader: ClassLoader): PackageManagerProvider {
    @Volatile
    override var packageManager: PackageManager? = null
        private set
    
    init {
        val systemServerClass =
            XposedHelpers.findClass("com.android.server.SystemServer", classLoader)
        val systemServerPackageManagerField = systemServerClass.getDeclaredField("mPackageManager")
        systemServerPackageManagerField.isAccessible = true

        XposedHelpers.findAndHookMethod(
            systemServerClass, "startBootstrapServices",
            XposedHelpers.findClass(
                "com.android.server.utils.TimingsTraceAndSlog",
                classLoader
            ),
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    try {
                        Logger.info("Get system's package manager ...")
                        super.afterHookedMethod(param)
                        param.hasThrowable() && return
                        val systemServerPackageManager = systemServerPackageManagerField.get(param.thisObject)
                        Logger.info(systemServerPackageManager)
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