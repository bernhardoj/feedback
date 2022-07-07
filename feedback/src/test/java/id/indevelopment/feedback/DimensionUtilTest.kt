package id.indevelopment.feedback

import android.content.res.Resources
import android.util.TypedValue
import id.indevelopment.feedback.util.DimensionUtil
import org.junit.Test
import org.mockito.Mockito.mockStatic
import org.mockito.kotlin.mock

internal class DimensionUtilTest {
    private val resources = mock<Resources>()

    @Test
    fun verifyMethodCalled() {
        val typedValue = mockStatic(TypedValue::class.java)
        val dp = 2f
        DimensionUtil.dpToPx(resources, dp)
        typedValue.verify {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        }
    }
}