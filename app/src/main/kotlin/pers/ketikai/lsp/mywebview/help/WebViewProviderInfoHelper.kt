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
import android.util.Base64
import org.lsposed.hiddenapibypass.HiddenApiBypass
import pers.ketikai.lsp.mywebview.logging.Logger
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

internal class WebViewProviderInfoHelper(
    private val classLoader: ClassLoader,
    private val logger: Logger,
    private val packageManagerProvider: PackageManagerProvider,
) {
    @SuppressLint("PrivateApi", "QueryPermissionsNeeded")
    fun findWebViewProviderInfoArray(): Any? {
        val packageManager = packageManagerProvider.packageManager
        if (packageManager == null) {
            logger.info("Package manager is not yet set, skip!")
            return null
        }
        logger.info("Get installed packages ...")
        val installedPackageInfoList =
            packageManager.getInstalledPackages(
                PackageManager.MATCH_ALL or PackageManager.GET_META_DATA or PackageManager.GET_SIGNING_CERTIFICATES,
            )
        val installedPackagesInfoListSize = installedPackageInfoList.size
        if (installedPackagesInfoListSize == 0) {
            logger.info("Found 0 installed packages, skip!")
            return null
        } else {
            logger.info("Found %s installed packages.", installedPackagesInfoListSize)
        }
        val webViewProviderInfoList = mutableListOf<Any>()
        val webViewProviderInfoClass =
            Class.forName(
                "android.webkit.WebViewProviderInfo",
                false,
                classLoader,
            )
        val webViewProviderInfoConstructor =
            HiddenApiBypass.getDeclaredConstructor(
                webViewProviderInfoClass,
                String::class.java,
                String::class.java,
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                Array<String>::class.java,
            )
        for (packageInfo in installedPackageInfoList) {
            val applicationInfo = packageInfo.applicationInfo ?: continue
            val metaData = applicationInfo.metaData ?: continue
            val awLib = metaData.getString("com.android.webview.WebViewLibrary")
            awLib.isNullOrEmpty() && continue
            val packageName = packageInfo.packageName
            val label =
                packageManager.getApplicationLabel(applicationInfo).toString()
            logger.info("Found a WebView:")
            logger.info("  package name: $packageName")
            logger.info("  label: $label")
            val signingInfo = packageInfo.signingInfo
            if (signingInfo == null) {
                logger.info("Signing info is null, skip!")
                continue
            }
            val apkContentsSigners = signingInfo.apkContentsSigners
            if (apkContentsSigners == null) {
                logger.info("Apk contents signers is null, skip!")
                continue
            }
            if (apkContentsSigners.isEmpty()) {
                logger.info("Apk contents signers is empty, skip!")
                continue
            }

            val signatures = arrayOfNulls<String>(apkContentsSigners.size)
            for ((index, signature) in apkContentsSigners.withIndex()) {
                val rawCert = signature.toByteArray()
                val certStream: InputStream = ByteArrayInputStream(rawCert)
                val certFactory = CertificateFactory.getInstance("X509")
                val x509Cert: X509Certificate =
                    certFactory.generateCertificate(certStream) as X509Certificate
                signatures[index] =
                    Base64.encodeToString(x509Cert.encoded, Base64.DEFAULT)
            }
            logger.info("  signatures size: ${signatures.size}")
            logger.debug("  signatures: ${signatures.contentToString()}")
            webViewProviderInfoList.add(
                webViewProviderInfoConstructor.newInstance(
                    packageName,
                    label,
                    true,
                    false,
                    signatures,
                ),
            )
        }
        val webViewProviderInfoListSize = webViewProviderInfoList.size
        if (webViewProviderInfoListSize <= 1) {
            logger.info("Any WebView not be found. Skip hook!")
            return null
        }
        val webViewProviders =
            java.lang.reflect.Array.newInstance(
                webViewProviderInfoClass,
                webViewProviderInfoListSize,
            )
        System.arraycopy(
            webViewProviderInfoList.toTypedArray(),
            0,
            webViewProviders,
            0,
            webViewProviderInfoListSize,
        )
        return webViewProviders
    }
}
