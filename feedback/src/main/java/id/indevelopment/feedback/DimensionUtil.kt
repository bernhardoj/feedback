package id.indevelopment.feedback

import android.content.res.Resources
import android.util.TypedValue

internal object DimensionUtil {
    fun dpToPx(res: Resources, dp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.displayMetrics)
}