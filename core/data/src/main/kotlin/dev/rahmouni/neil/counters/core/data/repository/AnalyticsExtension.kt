/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

internal fun AnalyticsHelper.logCreatedCounter() =
    logEvent(
        AnalyticsEvent(type = "created_counter"),
    )

internal fun AnalyticsHelper.logCreatedIncrement() =
    logEvent(
        AnalyticsEvent(type = "created_increment"),
    )
