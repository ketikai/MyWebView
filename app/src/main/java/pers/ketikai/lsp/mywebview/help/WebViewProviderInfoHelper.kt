package pers.ketikai.lsp.mywebview.help

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Base64
import de.robv.android.xposed.XposedHelpers
import pers.ketikai.lsp.mywebview.logging.Logger
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

internal class WebViewProviderInfoHelper(classLoader: ClassLoader, private val packageManagerProvider: PackageManagerProvider) {
    private val webViewProviderInfoClass = XposedHelpers.findClass(
        "android.webkit.WebViewProviderInfo", classLoader
    )

    private val webViewProviderInfoConstructor = XposedHelpers.findConstructorBestMatch(
        webViewProviderInfoClass,
        String::class.java,
        String::class.java,
        Boolean::class.javaPrimitiveType,
        Boolean::class.javaPrimitiveType,
        Array<String>::class.java
    )
    
    @SuppressLint("QueryPermissionsNeeded")
    fun findWebViewProviderInfoArray(): Any? {
        val packageManager = packageManagerProvider.packageManager
        if (packageManager == null) {
            Logger.info("Package manager is not yet set, skip!")
            return null
        }
        Logger.info("Get installed packages ...")
        val installedPackageInfoList =
            packageManager.getInstalledPackages(PackageManager.MATCH_ALL or PackageManager.GET_META_DATA or PackageManager.GET_SIGNING_CERTIFICATES)
        val installedPackagesInfoListSize = installedPackageInfoList.size
        if (installedPackagesInfoListSize == 0) {
            Logger.info("Found 0 installed packages, skip!")
            return null
        } else {
            Logger.info("Found %s installed packages.", installedPackagesInfoListSize)
        }
        val webViewProviderInfoList = mutableListOf<Any>()
        for (packageInfo in installedPackageInfoList) {
            val applicationInfo = packageInfo.applicationInfo ?: continue
            val metaData = applicationInfo.metaData ?: continue
            val awLib = metaData.getString("com.android.webview.WebViewLibrary")
            (awLib == null || awLib.isEmpty()) && continue
            val packageName = packageInfo.packageName
            val label =
                packageManager.getApplicationLabel(applicationInfo).toString()
            Logger.info("Found a WebView:")
            Logger.info("  package name: $packageName")
            Logger.info("  label: $label")
            val signingInfo = packageInfo.signingInfo
            if (signingInfo == null) {
                Logger.info("Signing info is null, skip!")
                continue
            }
            val apkContentsSigners = signingInfo.apkContentsSigners
            if (apkContentsSigners == null) {
                Logger.info("Apk contents signers is null, skip!")
                continue
            }
            if (apkContentsSigners.isEmpty()) {
                Logger.info("Apk contents signers is empty, skip!")
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
            Logger.info("  signatures size: ${signatures.size}")
            Logger.debug("  signatures: ${signatures.contentToString()}")
            webViewProviderInfoList.add(
                webViewProviderInfoConstructor.newInstance(
                    packageName,
                    label,
                    true,
                    false,
                    signatures
                )
            )
        }
        val webViewProviderInfoListSize = webViewProviderInfoList.size
        if (webViewProviderInfoListSize <= 1) {
            Logger.info("Any WebView not be found. Skip hook!")
            return null
        }
        val webViewProviders: Any = java.lang.reflect.Array.newInstance(
            webViewProviderInfoClass,
            webViewProviderInfoListSize
        )
        System.arraycopy(
            webViewProviderInfoList.toTypedArray(), 0, webViewProviders, 0,
            webViewProviderInfoListSize
        )
        return webViewProviderInfoList
    }
}