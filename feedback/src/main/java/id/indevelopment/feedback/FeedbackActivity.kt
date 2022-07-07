package id.indevelopment.feedback

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import id.indevelopment.feedback.databinding.ActivityFeedbackBinding
import id.indevelopment.feedback.util.LogUtil
import java.io.File
import java.util.*

internal class FeedbackActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFeedbackBinding.inflate(layoutInflater) }
    private lateinit var logs: List<SystemInfo>
    private lateinit var appName: String
    private lateinit var screenshotFile: File
    private lateinit var emailTo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)
        WindowCompat.getInsetsController(window, binding.root).isAppearanceLightStatusBars = true

        val packageName = intent.getStringExtra(PACKAGE_NAME) ?: ""
        val packageVersion = intent.getStringExtra(PACKAGE_VERSION) ?: ""
        val packageVersionName = intent.getStringExtra(PACKAGE_VERSION_NAME) ?: ""
        val locale = intent.getStringExtra(LOCALE) ?: ""
        val screenshotPath = intent.getStringExtra(SCREENSHOT_PATH) ?: ""
        appName = intent.getStringExtra(APP_NAME) ?: ""
        emailTo = intent.getStringExtra(EMAIL) ?: ""
        screenshotFile = File(screenshotPath)

        logs = LogUtil.buildLogs(
            packageName,
            packageVersion,
            packageVersionName,
            locale
        )

        binding.toolbar.setNavigationIcon(R.drawable.feedback_ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.send_feedback -> sendFeedback()
            }
            true
        }

        binding.viewLogs.setOnClickListener {
            LogDialog(logs).show(supportFragmentManager, LogDialog.TAG)
        }

        val uri = Uri.fromFile(screenshotFile)
        binding.screenshot.setImageURI(uri)
        binding.editButton.setOnClickListener {
            ScreenshotPreviewDialog(uri) {
                binding.screenshot.setImageURI(null)
                binding.screenshot.setImageURI(uri)
            }.show(supportFragmentManager, ScreenshotPreviewDialog.TAG)
        }

        binding.textField.addTextChangedListener {
            binding.toolbar.menu[0].isEnabled = it?.isNotEmpty() ?: false
        }
    }

    private fun sendFeedback() {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        val attachmentUris = arrayListOf<Uri>()
        if (binding.screenshotCheckbox.isChecked) {
            val uri = FileProvider.getUriForFile(this, "$packageName.provider", screenshotFile)
            attachmentUris.add(uri)
        }
        if (binding.logsCheckbox.isChecked) {
            val logsFile = LogUtil.writeLogsToFile(this, logs.toString())
            val uri = FileProvider.getUriForFile(this, "$packageName.provider", logsFile)
            attachmentUris.add(uri)
        }

        val body = StringBuilder().appendLine(binding.textField.text.toString())
            .appendLine()
            .append("Feedback ID: ${UUID.randomUUID()}")

        intent.apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_SUBJECT, "$appName Feedback")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTo))
            putExtra(Intent.EXTRA_TEXT, body.toString())
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentUris)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }.run {
            startActivity(Intent.createChooser(this, "Send Feedback"))
        }
    }

    companion object {
        const val EMAIL = "EMAIL"
        const val APP_NAME = "APP_NAME"
        const val PACKAGE_NAME = "PACKAGE_NAME"
        const val PACKAGE_VERSION = "PACKAGE_VERSION"
        const val PACKAGE_VERSION_NAME = "PACKAGE_VERSION_NAME"
        const val LOCALE = "LOCALE"
        const val SCREENSHOT_PATH = "SCREENSHOT_PATH"
    }
}