package id.indevelopment.feedback

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.indevelopment.feedback.databinding.FeedbackLogDialogBinding

internal class LogDialog(private val logs: List<SystemInfo>) : DialogFragment() {
    private var binding: FeedbackLogDialogBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FeedbackLogDialogBinding.inflate(layoutInflater)
        binding?.let {
            it.logList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = LogAdapter(logs)
            }
            it.logs.setOnClickListener {
                TextDialog(logs[logs.size-1].body).show(childFragmentManager, null)
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
    }
}