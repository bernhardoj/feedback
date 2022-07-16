package id.indevelopment.feedback

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.indevelopment.feedback.databinding.FeedbackLogDialogBinding
import java.util.ArrayList

internal class LogDialog : DialogFragment() {
    private var binding: FeedbackLogDialogBinding? = null
    private var logs: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            logs = it.getStringArrayList(LOG)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FeedbackLogDialogBinding.inflate(layoutInflater)
        binding?.let {
            it.logList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = LogAdapter(logs ?: listOf())
            }
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.feedback_system_logs)
            .setView(binding?.root)
            .create()
    }

    companion object {
        const val TAG = "TextDialog"
        private const val LOG = "LOG"

        fun newInstance(log: List<String>): LogDialog {
            val dialog = LogDialog()
            val args = Bundle().apply {
                putStringArrayList(LOG, log as ArrayList<String>)
            }
            dialog.arguments = args

            return dialog
        }
    }
}