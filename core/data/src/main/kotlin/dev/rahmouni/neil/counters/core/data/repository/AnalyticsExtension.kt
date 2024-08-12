/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

internal fun AnalyticsHelper.logAccessibilityAltTextPreferenceChanged(value: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "accessibility_altText_preference_changed",
            extras = listOf(
                Param(key = "accessibility_altText_preference", value = value.toString()),
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

internal fun AnalyticsHelper.logTravelModePreferenceChanged(value: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "travelmode_preference_changed",
            extras = listOf(
                Param(key = "travelmode_preference", value = value.toString()),
            ),
        ),
    )

internal fun AnalyticsHelper.logFriendsMainPreferenceChanged(value: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "friendsmain_preference_changed",
            extras = listOf(
                Param(key = "friendsmain_preference", value = value.toString()),
            ),
        ),
    )

internal fun AnalyticsHelper.logAddEventPost() =
    logEvent(
        AnalyticsEvent(type = "add_eventpost"),
    )

internal fun AnalyticsHelper.logAddFriendPost() =
    logEvent(
        AnalyticsEvent(type = "add_friendpost"),
    )

internal fun AnalyticsHelper.logAddFriend() =
    logEvent(
        AnalyticsEvent(type = "add_friend"),
    )

internal fun AnalyticsHelper.logAddPublicPost() =
    logEvent(
        AnalyticsEvent(type = "add_publicpost"),
    )

internal fun AnalyticsHelper.logAddTriagingPost() =
    logEvent(
        AnalyticsEvent(type = "add_triagingpost"),
    )
