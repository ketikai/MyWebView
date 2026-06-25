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

import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import org.lsposed.hiddenapibypass.HiddenApiBypass
import pers.ketikai.lsp.mywebview.hook.Hooker
import pers.ketikai.lsp.mywebview.hook.SystemImplHooker
import pers.ketikai.lsp.mywebview.logging.Logger
import java.util.function.Function

@Suppress("unused")
class MyWebView : XposedModule() {
    private val hookers = mutableListOf<Hooker>()

    private lateinit var logger: Logger

    private fun registerHookers(logger: Logger) {
        hookers.add(SystemImplHooker(this, logger))
    }

    private fun <R> applyHookers(
        event: String,
        action: Function<Hooker, R>,
    ): List<R> {
        val result = mutableListOf<R>()
        try {
            for (hooker in hookers) {
                logger.info("[$event] apply hooker: ${hooker.name}")
                result.add(action.apply(hooker))
            }
        } catch (e: Throwable) {
            logger.error(e)
            result.clear()
        }
        return result
    }

    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        super.onModuleLoaded(param)
        HiddenApiBypass.setHiddenApiExemptions("")
        this.logger = Logger(this)
        registerHookers(logger)
        applyHookers("onModuleLoaded") {
            it.onModuleLoaded(param)
        }
    }

    override fun onSystemServerStarting(param: XposedModuleInterface.SystemServerStartingParam) {
        super.onSystemServerStarting(param)
        applyHookers("onSystemServerStarting") {
            it.onSystemServerStarting(param)
        }
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        super.onPackageLoaded(param)
        applyHookers("onPackageLoaded") {
            it.onPackageLoaded(param)
        }
    }

    override fun onPackageReady(param: XposedModuleInterface.PackageReadyParam) {
        super.onPackageReady(param)
        applyHookers("onPackageReady") {
            it.onPackageReady(param)
        }
    }

    private fun isTrue(bool: Boolean): Boolean = bool

    override fun onHotReloading(param: XposedModuleInterface.HotReloadingParam): Boolean =
        super.onHotReloading(param) &&
            applyHookers("onHotReloading") {
                it.onHotReloading(param)
            }.all(this::isTrue)

    override fun onHotReloaded(param: XposedModuleInterface.HotReloadedParam) {
        super.onHotReloaded(param)
        applyHookers("onHotReloaded") {
            it.onHotReloaded(param)
        }
    }
}
