package id.indevelopment.sample_feedback

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.matcher.UriMatchers.*
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import id.indevelopment.feedback.R
import id.indevelopment.sample_feedback.matcher.Matcher.withImageDrawable
import id.indevelopment.sample_feedback.matcher.Matcher.withImageSize
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class FeedbackTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun openFeedbackWithScreenshotOfCurrentActivity() {
        val feedback = "this is my feedback"
        onView(withId(id.indevelopment.sample_feedback.R.id.button)).perform(click())

        // check if navigation icon is visible
        onView(withContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))

        // check toolbar title
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.feedback_send_feedback))))

        // check send button enable after feedback is not empty
        onView(withId(R.id.send_feedback)).check(matches(isNotEnabled()))
        onView(withId(R.id.textField)).perform(typeText(feedback))
        onView(withId(R.id.send_feedback)).check(matches(isEnabled()))

        // check logs dialog
        onView(withId(R.id.view_logs)).perform(click())
        onView(withId(R.id.logList)).inRoot(isDialog()).check(matches(allOf(isDisplayed())))
        onView(withId(R.id.logs)).perform(swipeUp(), click())
        onView(withId(android.R.id.message)).inRoot(isDialog()).check(matches(isDisplayed()))
        pressBack()

        // check screenshot dialog
        onView(withId(R.id.edit_button)).perform(click())
        onView(withId(R.id.screenshot_preview_save)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(R.id.screenshot_preview_undo)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(R.id.screenshot_preview_pick_highlight_color)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(R.id.screenshot_preview_pick_hide_color)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(R.id.screenshot_preview_image)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(R.id.screenshot_preview_image_drawable_view)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(R.id.screenshot_preview_close)).perform(click())
        onView(withId(R.id.screenshot_preview_image_drawable_view)).check(doesNotExist())

        // test send feedback (screenshot and logs)
        onView(withId(R.id.send_feedback)).perform(click())
        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtras(
                    allOf(
                        hasEntry(
                            Intent.EXTRA_INTENT, allOf(
                                hasAction(Intent.ACTION_SEND_MULTIPLE),
                                hasType("message/rfc822"),
                                hasFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
                                hasExtras(
                                    allOf(
                                        hasEntry(Intent.EXTRA_SUBJECT, "Feedback Feedback"),
                                        hasEntry(
                                            Intent.EXTRA_EMAIL,
                                            arrayOf("id.indevelopment@gmail.com")
                                        ),
                                        hasEntry(Intent.EXTRA_TEXT, containsString(feedback)),
                                        hasEntry(
                                            Intent.EXTRA_STREAM, allOf(
                                                hasItems(
                                                    hasScheme("content"),
                                                    hasPath("/cache/feedback_screenshot.png"),
                                                    hasPath("/cache/logs.txt"),
                                                    hasHost("id.indevelopment.sample_feedback.provider")
                                                ),
                                                hasSize<String>(2)
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        hasEntry(Intent.EXTRA_TITLE, "Send Feedback")
                    )
                )
            )
        )
    }

    @Test
    fun openFeedback_screenshotOnly() {
        val feedback = "this is my feedback"
        onView(withId(id.indevelopment.sample_feedback.R.id.button)).perform(click())

        onView(withId(R.id.textField)).perform(typeText(feedback))

        // test send feedback (screenshot)
        onView(withId(R.id.logs_checkbox)).perform(click())
        onView(withId(R.id.send_feedback)).perform(click())
        intended(
            hasExtras(
                hasEntry(
                    Intent.EXTRA_INTENT, hasExtras(
                        hasEntry(
                            Intent.EXTRA_STREAM, allOf(
                                hasItems(
                                    hasScheme("content"),
                                    hasPath("/cache/feedback_screenshot.png"),
                                    hasHost("id.indevelopment.sample_feedback.provider")
                                ),
                                hasSize<String>(1)
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun openFeedback_logsOnly() {
        val feedback = "this is my feedback"
        onView(withId(id.indevelopment.sample_feedback.R.id.button)).perform(click())

        onView(withId(R.id.textField)).perform(typeText(feedback))

        // test send feedback (logs)
        onView(withId(R.id.screenshot_checkbox)).perform(click())
        onView(withId(R.id.send_feedback)).perform(click())
        intended(
            hasExtras(
                hasEntry(
                    Intent.EXTRA_INTENT, hasExtras(
                        hasEntry(
                            Intent.EXTRA_STREAM, allOf(
                                hasItems(
                                    hasScheme("content"),
                                    hasPath("/cache/logs.txt"),
                                    hasHost("id.indevelopment.sample_feedback.provider")
                                ),
                                hasSize<String>(1)
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun openFeedback_nothing() {
        val feedback = "this is my feedback"
        onView(withId(id.indevelopment.sample_feedback.R.id.button)).perform(click())

        onView(withId(R.id.textField)).perform(typeText(feedback))

        // test send feedback (nothing)
        onView(withId(R.id.screenshot_checkbox)).perform(click())
        onView(withId(R.id.logs_checkbox)).perform(click())
        onView(withId(R.id.send_feedback)).perform(click())
        intended(
            hasExtras(
                hasEntry(
                    Intent.EXTRA_INTENT,
                    hasExtras(hasEntry(Intent.EXTRA_STREAM, hasSize<String>(0)))
                )
            )
        )
    }

    @Test
    fun openFeedbackWithProvidedBitmapAsScreenshot() {
        onView(withId(id.indevelopment.sample_feedback.R.id.button2)).perform(click())

        // check if the passed bitmap is shown on the screenshot
        onView(withId(R.id.screenshot)).check(matches(withImageDrawable(id.indevelopment.sample_feedback.R.drawable.ss)))
        onView(withId(R.id.screenshot)).check(matches(withImageSize(576, 1024)))
    }
}