package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class AppUpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val downloadUrl: String,
    val githubPageUrl: String,
    val releaseNotes: String,
    val isUpdateAvailable: Boolean
)

object AppUpdateChecker {
    const val CURRENT_VERSION_CODE = 2
    const val CURRENT_VERSION_NAME = "2.0.0"

    private const val GITHUB_VERSION_JSON_URL =
        "https://raw.githubusercontent.com/kkhmstorage/MAHALLU-LEDGER/main/version.json"

    suspend fun checkForUpdate(): Result<AppUpdateInfo> = withContext(Dispatchers.IO) {
        try {
            val url = URL(GITHUB_VERSION_JSON_URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 6000
            connection.readTimeout = 6000
            connection.requestMethod = "GET"

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()

                val json = JSONObject(response)
                val latestVersionCode = json.optInt("versionCode", CURRENT_VERSION_CODE)
                val latestVersionName = json.optString("versionName", CURRENT_VERSION_NAME)
                val downloadUrl = json.optString(
                    "downloadUrl",
                    "https://raw.githubusercontent.com/kkhmstorage/MAHALLU-LEDGER/main/MahalLedger.apk"
                )
                val githubPageUrl = json.optString(
                    "githubPageUrl",
                    "https://kkhmstorage.github.io/MAHALLU-LEDGER/"
                )
                val releaseNotes = json.optString("releaseNotes", "New stability and performance improvements.")

                val isUpdateAvailable = latestVersionCode > CURRENT_VERSION_CODE

                Result.success(
                    AppUpdateInfo(
                        versionCode = latestVersionCode,
                        versionName = latestVersionName,
                        downloadUrl = downloadUrl,
                        githubPageUrl = githubPageUrl,
                        releaseNotes = releaseNotes,
                        isUpdateAvailable = isUpdateAvailable
                    )
                )
            } else {
                Result.failure(Exception("HTTP error ${connection.responseCode}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun launchDownloadIntent(context: Context, downloadUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to web browser or toast
        }
    }
}
