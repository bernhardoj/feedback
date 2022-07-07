package id.indevelopment.feedback

import android.graphics.Bitmap
import java.io.FileOutputStream

internal object FileUtil {
    fun saveBitmap(bitmap: Bitmap, fos: FileOutputStream) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
    }
}