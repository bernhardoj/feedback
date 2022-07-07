package id.indevelopment.feedback

import android.graphics.Bitmap

internal object BitmapUtil {
    fun resize(bitmap: Bitmap, maxSize: Int): Bitmap {
        return if (bitmap.width <= maxSize && bitmap.height <= maxSize) bitmap
        else {
            val aspectRatio: Double
            val targetHeight: Int
            val targetWidth: Int

            if (bitmap.height >= bitmap.width) {
                aspectRatio = bitmap.width / bitmap.height.toDouble()
                targetHeight = maxSize
                targetWidth = (maxSize * aspectRatio).toInt()
            } else {
                aspectRatio = bitmap.height / bitmap.width.toDouble()
                targetHeight = (maxSize * aspectRatio).toInt()
                targetWidth = maxSize
            }

            Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
        }
    }
}