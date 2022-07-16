package id.indevelopment.feedback

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.indevelopment.feedback.databinding.FeedbackSystemDialogBinding

internal class SystemDialog : DialogFragment() {
    private var binding: FeedbackSystemDialogBinding? = null
    private var logs: SystemLog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            logs = it.getParcelable(LOGS)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FeedbackSystemDialogBinding.inflate(layoutInflater)
        binding?.let {
            it.logList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SystemAdapter(logs?.systemInfo ?: listOf())
            }
            it.logs.setOnClickListener {
                LogDialog.newInstance(logs?.log ?: listOf()).show(childFragmentManager, LogDialog.TAG)
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

        fun newInstance(logs: SystemLog): SystemDialog {
            val dialog = SystemDialog()
            val args = Bundle().apply {
                putParcelable(LOGS, logs)
            }
            dialog.arguments = args

            return dialog
        }
    }
}