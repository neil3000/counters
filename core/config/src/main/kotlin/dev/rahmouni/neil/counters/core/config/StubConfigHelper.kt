package dev.rahmouni.neil.counters.core.config

import android.app.Activity
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of `ConfigHelper` that fetches the value from the Firebase Remote Config backend.
 */
@Singleton
internal class StubConfigHelper @Inject constructor() : ConfigHelper {

    override fun init(activity: Activity) {}

    override fun getLastFetchStatus(): String {
        return "Not available in Preview"
    }

    override fun getBoolean(key: String): Boolean {
        return false
    }

    override fun getString(key: String): String {
        return "Not available in Preview"
    }

    override fun forEachEntry(action: ((key: String, value: String, source: String) -> Unit)) {}
}
