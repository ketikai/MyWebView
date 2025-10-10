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

package pers.ketikai.lsp.mywebview

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import pers.ketikai.lsp.mywebview.hook.Hooker
import pers.ketikai.lsp.mywebview.hook.SystemImplHooker
import pers.ketikai.lsp.mywebview.logging.Logger

@Suppress("unused")
class MyWebView :
    IXposedHookZygoteInit,
    IXposedHookLoadPackage {
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
