package dev.rahmouni.neil.counters.core.testing.util

import dev.rahmouni.neil.counters.core.analytics.AnalyticsEvent
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper

class TestAnalyticsHelper : AnalyticsHelper {

    override var appInstallationID: String = "Not available in Tests"

    private val events = mutableListOf<AnalyticsEvent>()
    override fun logEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    override fun clearMetrics() {}

    fun hasLogged(event: AnalyticsEvent) = event in events
}
