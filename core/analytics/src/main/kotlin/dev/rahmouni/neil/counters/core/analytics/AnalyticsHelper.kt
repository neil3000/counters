package dev.rahmouni.neil.counters.core.analytics

/**
 * Interface for logging analytics events. See `FirebaseAnalyticsHelper` and
 * `StubAnalyticsHelper` for implementations.
 */
interface AnalyticsHelper {
    var appInstallationID: String

    fun logEvent(event: AnalyticsEvent)
    fun clearMetrics()
}
