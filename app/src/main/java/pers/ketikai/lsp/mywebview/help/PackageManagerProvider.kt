package pers.ketikai.lsp.mywebview.help

import android.content.pm.PackageManager

internal interface PackageManagerProvider {

    val packageManager: PackageManager?
}