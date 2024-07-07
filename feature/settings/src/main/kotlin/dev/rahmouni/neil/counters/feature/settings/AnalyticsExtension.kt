/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.settings

import dev.rahmouni.neil.counters.core.analytics.AnalyticsEvent
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper

internal fun AnalyticsHelper.logAccessibilitySettingsUiEvent(uiId: String) =
    logEvent(
        AnalyticsEvent(
            type = "accessibilitySettings_${uiId}_clicked",
        ),
    )

internal fun AnalyticsHelper.logDataAndPrivacySettingsUiEvent(uiId: String) =
    logEvent(
        AnalyticsEvent(
            type = "dataAndPrivacySettings_${uiId}_clicked",
        ),
    )

internal fun AnalyticsHelper.logSettingsUiEvent(uiId: String) =
    logEvent(
        AnalyticsEvent(
            type = "settings_${uiId}_clicked",
        ),
    )
