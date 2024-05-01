package dev.rahmouni.neil.counters.core.config

import android.app.Activity
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable

interface ConfigHelper {
    fun init(activity: Activity)
    fun getLastFetchStatus(): String
    fun getBoolean(key: String): Boolean
    fun getString(key: String): String
    fun getRolloutFlag(id: Int): Boolean {
        return getBoolean("i_{id}")
    }
    fun forEachEntry(action: ((key: String, value: String, source: String) -> Unit))
}
