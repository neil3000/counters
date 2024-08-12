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
    val date: Date,
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
        date = date?.toDate() ?: Date(),
        private = private,
    )
}

