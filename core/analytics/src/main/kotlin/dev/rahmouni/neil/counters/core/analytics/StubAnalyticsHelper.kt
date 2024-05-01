package dev.rahmouni.neil.counters.core.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "StubAnalyticsHelper"

/**
 * An implementation of AnalyticsHelper just writes the events to logcat. Used in builds where no
 * analytics events should be sent to a backend.
 */
@Singleton
internal class StubAnalyticsHelper @Inject constructor() : AnalyticsHelper {

    override var appInstallationID: String = "Not available in Demo"

    override fun logEvent(event: AnalyticsEvent) {
        Log.d(TAG, "Received analytics event: $event")
    }

    override fun clearMetrics() {
        Log.d(TAG, "Cleared metrics")
    }
}
