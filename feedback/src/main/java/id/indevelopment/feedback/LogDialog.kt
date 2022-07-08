package id.indevelopment.feedback

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.indevelopment.feedback.databinding.FeedbackLogDialogBinding

internal class LogDialog : DialogFragment() {
    private var binding: FeedbackLogDialogBinding? = null
    private var logs: ArrayList<SystemInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            logs = it.getParcelableArrayList(LOGS)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FeedbackLogDialogBinding.inflate(layoutInflater)
        binding?.let {
            it.logList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = LogAdapter(logs ?: listOf())
            }
            it.logs.setOnClickListener {
                TextDialog.newInstance(logs?.last()?.body ?: "").show(childFragmentManager, null)
            }
        }
        return AlertDialog.Builder(requireContext())
            .setView(binding?.root)
            .setTitle(R.string.feedback_dialog_title)
            .create()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "LogDialog"
        private const val LOGS = "LOGS"

        fun newInstance(logs: ArrayList<SystemInfo>): LogDialog {
            val dialog = LogDialog()
            val args = Bundle().apply {
                putParcelableArrayList(LOGS, logs)
            }
            dialog.arguments = args

            return dialog
        }
    }
}