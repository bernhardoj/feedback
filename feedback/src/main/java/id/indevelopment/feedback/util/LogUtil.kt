package id.indevelopment.feedback.util

import android.content.Context
import android.os.Build
import android.os.Process
import android.util.Log
import id.indevelopment.feedback.SystemInfo
import java.io.*

internal object LogUtil {
    private val TAG: String? = LogUtil::class.java.canonicalName

    private fun getLogcat(): String {
        val logcatCmd = arrayOf("logcat", "-d", "-v", "threadtime")
        val pid = Process.myPid().toString()
        val builder = StringBuilder()

        var isr: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        try {
            val process = Runtime.getRuntime().exec(logcatCmd)
            isr = InputStreamReader(process.inputStream)
            bufferedReader = BufferedReader(isr)
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                if (line?.contains(pid) == true) {
                    builder.appendLine(line)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "getLogcat error: $e")
            return ""
        } finally {
            try {
                isr?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close InputStreamReader", e)
            } finally {
                try {
                    bufferedReader?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Could not close BufferedReader", e)
                }
            }
        }
        return builder.toString()
    }

    fun buildLogs(
        packageName: String,
        packageVersion: String,
        packageVersionName: String,
        locale: String
    ): List<SystemInfo> {
        val infos = mutableListOf<SystemInfo>()
        infos.add(SystemInfo("Package name", packageName))
        infos.add(SystemInfo("Package version", packageVersion))
        infos.add(SystemInfo("Package version name", packageVersionName))
        infos.add(SystemInfo("Locale", locale))
        infos.add(SystemInfo("Device", Build.DEVICE))
        infos.add(SystemInfo("Build ID", Build.ID))
        infos.add(SystemInfo("Build type", Build.TYPE))
        infos.add(SystemInfo("Build fingerprint", Build.FINGERPRINT))
        infos.add(SystemInfo("Model", Build.MODEL))
        infos.add(SystemInfo("Product", Build.PRODUCT))
        infos.add(SystemInfo("SDK Version", Build.VERSION.SDK_INT.toString()))
        infos.add(SystemInfo("Release", Build.VERSION.RELEASE))
        infos.add(SystemInfo("Incremental version", Build.VERSION.INCREMENTAL))
        infos.add(SystemInfo("Codename", Build.VERSION.CODENAME))
        infos.add(SystemInfo("Board", Build.BOARD))
        infos.add(SystemInfo("Brand", Build.BRAND))
        infos.add(SystemInfo("Logs", getLogcat()))

        return infos
    }

    fun writeLogsToFile(context: Context, logs: String): File {
        val file = File(context.cacheDir, "logs.txt")
        var out: PrintWriter? = null

        try {
            // Empty out file first
            PrintWriter(file).close()

            out = PrintWriter(BufferedWriter(FileWriter(file, true)))
            out.print(logs)
        } catch (e: Exception) {
            Log.e(TAG, "Write error: $e")
        } finally {
            out?.close()
        }

        return file
    }
}