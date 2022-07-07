package id.indevelopment.feedback

import android.content.Context
import android.os.Build
import android.os.Process
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.core.text.bold
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
    ): SpannableStringBuilder {
        val logs = SpannableStringBuilder()
        logs.bold { logs.appendLine("Package name") }
            .appendLine(packageName)
            .appendLine()

        logs.bold { logs.appendLine("Package version") }
            .appendLine(packageVersion)
            .appendLine()

        logs.bold { logs.appendLine("Package version name") }
            .appendLine(packageVersionName)
            .appendLine()

        logs.bold { logs.appendLine("Locale") }
            .appendLine(locale)
            .appendLine()

        logs.bold { logs.appendLine("Device") }
            .appendLine(Build.DEVICE)
            .appendLine()

        logs.bold { logs.appendLine("Build ID") }
            .appendLine(Build.ID)
            .appendLine()

        logs.bold { logs.appendLine("Build type") }
            .appendLine(Build.TYPE)
            .appendLine()

        logs.bold { logs.appendLine("Build fingerprint") }
            .appendLine(Build.FINGERPRINT)
            .appendLine()

        logs.bold { logs.appendLine("Model") }
            .appendLine(Build.MODEL)
            .appendLine()

        logs.bold { logs.appendLine("Product") }
            .appendLine(Build.PRODUCT)
            .appendLine()

        logs.bold { logs.appendLine("SDK Version") }
            .appendLine(Build.VERSION.SDK_INT.toString())
            .appendLine()

        logs.bold { logs.appendLine("Release") }
            .appendLine(Build.VERSION.RELEASE)
            .appendLine()

        logs.bold { logs.appendLine("Incremental version") }
            .appendLine(Build.VERSION.INCREMENTAL)
            .appendLine()

        logs.bold { logs.appendLine("Codename") }
            .appendLine(Build.VERSION.CODENAME)
            .appendLine()

        logs.bold { logs.appendLine("Board") }
            .appendLine(Build.BOARD)
            .appendLine()

        logs.bold { logs.appendLine("Brand") }
            .appendLine(Build.BRAND)
            .appendLine()

        logs.bold { logs.appendLine("Log") }
            .appendLine(getLogcat())
            .appendLine()

        return logs
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