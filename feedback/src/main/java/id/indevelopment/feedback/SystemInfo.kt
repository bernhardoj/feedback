package id.indevelopment.feedback

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SystemInfo(val title: String, val body: String): Parcelable
