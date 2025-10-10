package pers.ketikai.lsp.mywebview

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import pers.ketikai.lsp.mywebview.hook.Hooker
import pers.ketikai.lsp.mywebview.hook.SystemImplHooker
import pers.ketikai.lsp.mywebview.logging.Logger

@Suppress("unused")
class MyWebView : IXposedHookZygoteInit, IXposedHookLoadPackage {

    private val hookers = mutableListOf<Hooker>()

    private fun registerHookers() {
        hookers.add(SystemImplHooker())
    }

    override fun initZygote(startupParam: StartupParam) {
        try {
            registerHookers()
            for (hooker in hookers) {
                hooker !is IXposedHookZygoteInit && continue
                Logger.info("Apply \"initZygote\" hooker: %s", hooker.name)
                hooker.initZygote(startupParam)
            }
        } catch (e: Throwable) {
            Logger.info(e)
        }
    }

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        try {
            for (hooker in hookers) {
                hooker !is IXposedHookLoadPackage && continue
                Logger.info("Apply \"handleLoadPackage\" hooker: %s", hooker.name)
                hooker.handleLoadPackage(lpparam)
            }
        } catch (e: Throwable) {
            Logger.info(e)
        }
    }
}