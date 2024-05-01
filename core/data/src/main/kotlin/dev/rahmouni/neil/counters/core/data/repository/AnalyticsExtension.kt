package dev.rahmouni.neil.counters.core.data.repository

import dev.rahmouni.neil.counters.core.analytics.AnalyticsEvent
import dev.rahmouni.neil.counters.core.analytics.AnalyticsEvent.Param
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper

internal fun AnalyticsHelper.logAccessibilityEmphasizedSwitchesPreferenceChanged(value: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "accessibility_emphasizedSwitches_preference_changed",
            extras = listOf(
                Param(
                    key = "accessibility_emphasizedSwitches_preference",
                    value = value.toString(),
                ),
            ),
        ),
    )

internal fun AnalyticsHelper.logAccessibilityIconTooltipsPreferenceChanged(value: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "accessibility_iconTooltips_preference_changed",
            extras = listOf(
                Param(key = "accessibility_iconTooltips_preference", value = value.toString()),
            ),
        ),
    )

internal fun AnalyticsHelper.logMetricsPreferenceChanged(value: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "metrics_preference_changed",
            extras = listOf(
                Param(key = "metrics_preference", value = value.toString()),
            ),
        ),
    )

internal fun AnalyticsHelper.logCrashlyticsPreferenceChanged(value: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "crashlytics_preference_changed",
            extras = listOf(
                Param(key = "crashlytics_preference", value = value.toString()),
            ),
        ),
    )