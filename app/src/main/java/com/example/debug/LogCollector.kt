package com.example.catsinapp.debug

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object LogCollector {

    /**
     * Собирает логи за последнюю минуту и сохраняет в файл.
     * Возвращает Uri файла для шаринга.
     */
    fun collectAndShare(context: Context) {
        try {
            // Логи за последние 60 секунд через logcat -t
            val process = Runtime.getRuntime().exec(
                arrayOf("logcat", "-d", "-t", "60", "--pid=${android.os.Process.myPid()}")
            )
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val logs   = reader.readText()
            reader.close()

            // Сохраняем в кэш файл
            val logFile = File(context.cacheDir, "cats_app_log_${System.currentTimeMillis()}.txt")
            logFile.writeText(logs)

            // Шарим через Intent
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                logFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type    = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "CatsInApp — logs (last 60s)")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Save logs via..."))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
