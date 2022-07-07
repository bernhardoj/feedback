package id.indevelopment.feedback

import android.graphics.Bitmap
import id.indevelopment.feedback.util.FileUtil
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.io.FileOutputStream

internal class FileUtilTest {
    private val bitmap = mock<Bitmap>()
    private val fos = mock<FileOutputStream>()

    @Test
    fun convertCorrectly() {
        FileUtil.saveBitmap(bitmap, fos)
        verify(bitmap).compress(Bitmap.CompressFormat.PNG, 100, fos)
        verify(fos).close()
    }
}