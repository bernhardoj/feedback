package id.indevelopment.feedback

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

internal class TextDialog(private val text: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.feedback_system_logs)
            .setMessage(text)
            .create()
    }

    companion object {
        const val TAG = "TextDialog"
    }
}