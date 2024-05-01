package dev.rahmouni.neil.counters.feature.settings

import dev.rahmouni.neil.counters.core.analytics.AnalyticsEvent
import dev.rahmouni.neil.counters.core.analytics.AnalyticsEvent.Param
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper

internal fun AnalyticsHelper.logAndroidAccessibilityTileClicked() =
    logEvent(
        AnalyticsEvent(
            type = "accessibility_androidAccessibilityTile_clicked",
        ),
    )

internal fun AnalyticsHelper.logChangelogTileClicked() =
    logEvent(
        AnalyticsEvent(
            type = "settings_changelogTile_clicked",
        ),
    )

internal fun AnalyticsHelper.logClearMetricsTileClicked() =
    logEvent(
        AnalyticsEvent(
            type = "dataAndPrivacy_clearMetricsTile_clicked",
        ),
    )