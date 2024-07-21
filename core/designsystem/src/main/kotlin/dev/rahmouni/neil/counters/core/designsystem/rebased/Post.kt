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

package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.designsystem.R.string
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Post(
    val id: String,
    val userId: String,
    val sharingScope: SharingScope = SharingScope.BUILDING,
    val location: String,
    val timestamp: LocalDateTime,
    val feed: FeedType = FeedType.PUBLIC,
    val categories: List<String> = emptyList(),
    val content: String,
    val postType: PostType,
    val additionalInfos: List<String> = emptyList(),
) {
    @Composable
    fun timeElapsed(): String {
        val now = LocalDateTime.now()
        val totalMinutes = ChronoUnit.MINUTES.between(timestamp, now)

        return when {
            totalMinutes >= (24 * 60) -> "${totalMinutes / (24 * 60)}" + stringResource(string.core_designsystem_timeElapsed_day)
            totalMinutes >= 60 -> "${totalMinutes / 60}" + stringResource(string.core_designsystem_timeElapsed_hours)
            totalMinutes > 0 -> "$totalMinutes" + stringResource(string.core_designsystem_timeElapsed_minute)
            else -> stringResource(string.core_designsystem_timeElapsed_now)
        }
    }
}
