package id.indevelopment.feedback

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

internal class TextDialog : DialogFragment() {
    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            text = it.getString(TEXT, "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.feedback_system_logs)
            .setMessage(text)
            .create()
    }

    companion object {
        const val TAG = "TextDialog"
        private const val TEXT = "TEXT"

        fun newInstance(text: String): TextDialog {
            val dialog = TextDialog()
            val args = Bundle().apply {
                putString(TEXT, text)
            }
            dialog.arguments = args

            return dialog
        }
    }
}