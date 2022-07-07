package id.indevelopment.feedback

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*

internal class LogUtilTest {
    @Test
    fun buildLogsTest() {
        val packageName = "id.indevelopment.feedback"
        val packageVersion = "1"
        val packageVersionName = "1.0"
        val locale = "en"
        val mockedDevice = "my device"
        val buildClassProvider = mock<BuildClassProvider> {
            on { device } doReturn mockedDevice
        }
        var logString = ""
        val logs = mock<SpannableStringBuilder> {
//            on { bold {} } doReturn SpannableStringBuilder()
            on { append(any<String>()) } doAnswer {
                logString += it.getArgument<String>(0)
                SpannableStringBuilder()
            }
            on { appendLine() } doAnswer {
                logString += "\n"
                SpannableStringBuilder()
            }
            on { appendLine(any<String>()) } doAnswer {
                logString += "${it.getArgument<String>(0)}\n"
                SpannableStringBuilder()
            }
        }

//        doAnswer {  }.whenever(logs).append(any<String>(), any(), any())

        doNothing().whenever(logs).setSpan(any(), any(), any(), any())

        LogUtil.buildLogs(logs, packageName, packageVersion, packageVersionName, locale, buildClassProvider)

//        verify(logs).bold { }
//        verify(logs).appendLine("Package name")
//        verify(logs).appendLine(packageName)
//        verify(logs).appendLine()
        assertEquals("""
            Package name
            $packageName

            Package version
            $packageVersion

            Package version name
            $packageVersionName

            Locale
            $locale

            Device
            $mockedDevice


        """.trimIndent(), logString)
    }
}