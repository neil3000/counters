package dev.rahmouni.neil.counters.core.config

import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.text.format.DateUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import java.text.DateFormat
import javax.inject.Inject

/**
 * Implementation of `ConfigHelper` that fetches the value from the Firebase Remote Config backend.
 */
internal class FirebaseConfigHelper @Inject constructor(
    private val firebaseConfig: FirebaseRemoteConfig,
) : ConfigHelper {

    override fun init(activity: Activity) {
        firebaseConfig.setDefaultsAsync(R.xml.core_config_remote_config_defaults)
        firebaseConfig.fetchAndActivate()
    }

    @Suppress("SpellCheckingInspection")
    override fun getLastFetchStatus(): String {
        return when (firebaseConfig.info.lastFetchStatus) {
            FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS -> "SUCCESS (${DateUtils.getRelativeTimeSpanString(firebaseConfig.info.fetchTimeMillis)})"
            FirebaseRemoteConfig.LAST_FETCH_STATUS_FAILURE -> "FAILURE"
            FirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED -> "THROTTLED"
            FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET -> "NO_FETCH_YET"
            else -> "RahNeil_N3:Error:NMdUsSOSmdgHuvcFuFr6WjorE25ZszWZ"
        }
    }

    override fun getBoolean(key: String): Boolean {
        return firebaseConfig.getBoolean(key)
    }

    override fun getString(key: String): String {
        return firebaseConfig.getString(key)
    }

    override fun forEachEntry(action: ((key: String, value: String, source: String) -> Unit)) {
        firebaseConfig.all.entries.forEach { (key, value) -> action(key, value.asString(), when(value.source) {
            FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> "remote"
            FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT -> "default"
            else -> "static"
        }) }
    }
}
