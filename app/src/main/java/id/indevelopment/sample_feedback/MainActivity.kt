package id.indevelopment.sample_feedback

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.indevelopment.feedback.Feedback
import id.indevelopment.sample_feedback.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            Feedback(this, emailTo = "id.indevelopment@gmail.com").openFeedback()
        }

        binding.button2.setOnClickListener {
            Feedback(
                this,
                emailTo = "id.indevelopment@gmail.com",
                screenCapture = BitmapFactory.decodeResource(resources, R.drawable.ss)
            ).openFeedback()
        }
    }
}