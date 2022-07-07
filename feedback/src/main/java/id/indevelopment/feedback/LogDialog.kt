package id.indevelopment.feedback

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.DialogFragment

internal class LogDialog(private val logs: SpannableStringBuilder) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(logs)
            .setTitle(R.string.feedback_dialog_title)
            .create()

    companion object {
        const val TAG = "LogDialog"
    }
}