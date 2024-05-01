package dev.rahmouni.neil.counters.core.analytics

/**
 * Implementation of AnalyticsHelper which does nothing. Useful for tests and previews.
 */
class NoOpAnalyticsHelper : AnalyticsHelper {

    override var appInstallationID: String = "Not available in Preview"

    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun clearMetrics() = Unit
}
