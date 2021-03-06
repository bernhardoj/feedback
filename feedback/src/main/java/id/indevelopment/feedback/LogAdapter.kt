package id.indevelopment.feedback

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.indevelopment.feedback.databinding.FeedbackLogItemBinding

class LogAdapter(private val logs: List<String>) : RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FeedbackLogItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(log: String) {
            binding.body.text = log
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FeedbackLogItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int = logs.size - 1
}