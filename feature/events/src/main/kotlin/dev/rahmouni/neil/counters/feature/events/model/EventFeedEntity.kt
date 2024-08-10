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

package dev.rahmouni.neil.counters.feature.events.model

import dev.rahmouni.neil.counters.core.data.model.EventFeedRawData
import dev.rahmouni.neil.counters.core.designsystem.rebased.SharingScope
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

data class EventFeedEntity(
    val uid: String,
    val userId: String?,
    val sharingScope: SharingScope,
    val createdAt: LocalDateTime,
    val title: String?,
    val description: String?,
    val location: String?,
    val date: Date?,
    val private: Boolean,
)

fun EventFeedRawData.toEntity(): EventFeedEntity {
    if (uid == null) throw IllegalStateException("Attempted to convert a EventRawData with null uid to a EventEntity")

    return EventFeedEntity(
        uid = uid!!,
        userId = userId,
        sharingScope = SharingScope.fromString(sharingScope),
        createdAt = createdAt.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
        title = title,
        description = description,
        location = location,
        date = date?.toDate(),
        private = private,
    )
}

