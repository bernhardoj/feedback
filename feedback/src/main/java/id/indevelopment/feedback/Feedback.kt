package id.indevelopment.feedback

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileOutputStream

class Feedback(
    private val callerActivity: Activity,
    private val screenCapture: Bitmap? = null,
    private val emailTo: String
) {
    @Suppress("DEPRECATION")
    fun openFeedback() {
        Log.d("TAG", "openFeedback: start ${System.currentTimeMillis()}")
        val bitmapCapture = screenCapture ?: callerActivity.window.decorView.drawToBitmap()
        val file = File(callerActivity.cacheDir, "feedback_screenshot.png")
        FileUtil.saveBitmap(BitmapUtil.resize(bitmapCapture, 1024), FileOutputStream(file))
        val screenshotPath = file.absolutePath

        val packageName = callerActivity.packageName
        val packageInfo =
            callerActivity.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
        val locale =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) callerActivity.resources.configuration.locales[0]
            else callerActivity.resources.configuration.locale
        val versionCode =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode
            else packageInfo.versionCode
        val appName = callerActivity.applicationInfo.loadLabel(callerActivity.packageManager)

        Intent(callerActivity, FeedbackActivity::class.java).apply {
            putExtra(FeedbackActivity.SCREENSHOT_PATH, screenshotPath)
            putExtra(FeedbackActivity.PACKAGE_NAME, packageName)
            putExtra(FeedbackActivity.PACKAGE_VERSION, versionCode.toString())
            putExtra(FeedbackActivity.PACKAGE_VERSION_NAME, packageInfo.versionName)
            putExtra(FeedbackActivity.LOCALE, locale.toString())
            putExtra(FeedbackActivity.APP_NAME, appName)
            putExtra(FeedbackActivity.EMAIL, emailTo)
        }.run { callerActivity.startActivity(this) }
    }
}