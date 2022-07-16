package id.indevelopment.feedback

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.indevelopment.feedback.databinding.FeedbackSystemItemBinding

class SystemAdapter(private val logs: List<SystemInfo>) : RecyclerView.Adapter<SystemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FeedbackSystemItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(info: SystemInfo) {
            binding.title.text = info.title
            binding.body.text = info.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FeedbackSystemItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int = logs.size - 1
}