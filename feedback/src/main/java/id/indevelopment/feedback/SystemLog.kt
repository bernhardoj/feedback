package id.indevelopment.feedback

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SystemLog(val systemInfo: List<SystemInfo>, val log: List<String>) : Parcelable