package id.indevelopment.sample_feedback.matcher

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.widget.ImageView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object Matcher {
    fun withImageSize(w: Int, h: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has image size ${w}x$h")
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                return sameSize(imageView.drawable, w, h)
            }
        }
    }

    fun withImageDrawable(resourceId: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has image drawable resource $resourceId")
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                return sameBitmap(imageView.context, imageView.drawable, resourceId)
            }
        }
    }

    private fun sameSize(drawable: Drawable?, w: Int, h: Int): Boolean {
        var aDrawable = drawable
        if (aDrawable is StateListDrawable) {
            aDrawable = aDrawable.getCurrent()
        }
        if (aDrawable is BitmapDrawable) {
            val bitmap = aDrawable.bitmap
            return bitmap.width == w && bitmap.height == h
        }
        return false
    }

    private fun sameBitmap(context: Context, drawable: Drawable?, resourceId: Int): Boolean {
        if (drawable == null) return false

        var aDrawable = drawable
        val expectedBitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        if (aDrawable is StateListDrawable) {
            aDrawable = aDrawable.getCurrent()
        }
        if (aDrawable is BitmapDrawable) {
            val bitmap = aDrawable.bitmap
            return bitmap.sameAs(expectedBitmap)
        }
        return false
    }
}