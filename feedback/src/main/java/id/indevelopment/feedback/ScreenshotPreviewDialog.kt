package id.indevelopment.feedback

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.DialogFragment
import id.indevelopment.feedback.databinding.FeedbackScreenshotPreviewBinding
import id.indevelopment.feedback.util.DimensionUtil
import id.indevelopment.feedback.util.FileUtil
import me.panavtec.drawableview.DrawableViewConfig
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

internal class ScreenshotPreviewDialog : DialogFragment() {
    private var binding: FeedbackScreenshotPreviewBinding? = null
    private val highlightColor by lazy {
        ContextCompat.getColor(
            requireContext(),
            R.color.feedback_highlight_color
        )
    }
    private val hideColor by lazy { ContextCompat.getColor(requireContext(), R.color.feedback_hide_color) }
    private var uri: Uri? = null
    private lateinit var listener: ScreenshotPreviewListener

    private val config by lazy {
        DrawableViewConfig().apply {
            isShowCanvasBounds = true
            strokeWidth = 57.0f
            minZoom = 1.0f
            maxZoom = 1.0f
            strokeColor = highlightColor
            val decorView = requireActivity().window.decorView
            canvasWidth = decorView.width
            canvasHeight = decorView.height
        }
    }

    interface ScreenshotPreviewListener {
        fun onSave()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ScreenshotPreviewListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement ScreenshotPreviewListener"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getParcelable(URI)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        binding = FeedbackScreenshotPreviewBinding.inflate(LayoutInflater.from(requireContext()))

        binding?.let {
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCanceledOnTouchOutside(false)
                setContentView(it.root)
            }

            it.screenshotPreviewImage.setImageURI(uri)
            it.screenshotPreviewImageDrawableView.apply {
                setConfig(config)
                bringToFront()
            }
            it.screenshotPreviewPickHighlightColor.setOnClickListener { _ ->
                config.strokeColor = highlightColor
                val highlightButton =
                    it.screenshotPreviewPickHighlightColor.drawable.mutate() as GradientDrawable
                val hideButton =
                    it.screenshotPreviewPickHideColor.drawable.mutate() as GradientDrawable
                highlightButton.setStroke(
                    DimensionUtil.dpToPx(resources, 2f).toInt(),
                    ContextCompat.getColor(requireContext(), R.color.feedback_border_color)
                )
                hideButton.setStroke(
                    DimensionUtil.dpToPx(resources, 0f).toInt(),
                    ContextCompat.getColor(requireContext(), R.color.feedback_border_color)
                )
            }
            it.screenshotPreviewPickHideColor.setOnClickListener { _ ->
                config.strokeColor = hideColor
                val hideButton =
                    it.screenshotPreviewPickHideColor.drawable.mutate() as GradientDrawable
                val highlightButton =
                    it.screenshotPreviewPickHighlightColor.drawable.mutate() as GradientDrawable
                hideButton.setStroke(
                    DimensionUtil.dpToPx(resources, 2f).toInt(),
                    ContextCompat.getColor(requireContext(), R.color.feedback_border_color)
                )
                highlightButton.setStroke(
                    DimensionUtil.dpToPx(resources, 0f).toInt(),
                    ContextCompat.getColor(requireContext(), R.color.feedback_border_color)
                )
            }
            it.screenshotPreviewClose.setOnClickListener {
                dismiss()
            }
            it.screenshotPreviewUndo.setOnClickListener { _ ->
                it.screenshotPreviewImageDrawableView.undo()
            }
            it.screenshotPreviewSave.setOnClickListener { _ ->
                Executors.newSingleThreadExecutor().execute {
                    FileUtil.saveBitmap(
                        it.screenshotPreviewImageViewUpdated.drawToBitmap(),
                        FileOutputStream(File(requireContext().cacheDir, "feedback_screenshot.png"))
                    )
                    listener.onSave()
                    dismiss()
                }
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val TAG = "ScreenshotPreviewDialog"
        private const val URI = "URI"

        fun newInstance(uri: Uri): ScreenshotPreviewDialog {
            val dialog = ScreenshotPreviewDialog()
            val args = Bundle().apply {
                putParcelable(URI, uri)
            }
            dialog.arguments = args

            return dialog
        }
    }
}