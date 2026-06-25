import logic.LogicMetadata

private fun loadVersionCode(): Int {
    return providers.exec {
        commandLine("git", "rev-list", "--first-parent", "--count", "HEAD")
    }.standardOutput.asText.getOrElse("0").trim().toInt()
}

private fun loadVersionName(): String {
    return generateVersionName() ?: "0.1.0"
}

private fun generateVersionName(): String? {
    val versionName = try {
        providers.exec {
            commandLine("git", "describe", "--tags", "--dirty", "--exclude", "*-*")
        }.standardOutput.asText.orNull?.trim()
    } catch (_: Exception) {
        null
    }
    versionName ?: return null
    return if (versionName.contains("-")) {
        versionName.replace(Regex("^(\\d+\\.\\d+\\.)(\\d+)")) {
            val groups = it.groups
            groups[1]!!.value + groups[2]!!.value.toInt().inc()
        }
    } else {
        versionName
    }
}

LogicMetadata.COMMIT_CODE = loadVersionCode()
LogicMetadata.VERSION_NAME = loadVersionName()

rootProject.name = LogicMetadata.NAME