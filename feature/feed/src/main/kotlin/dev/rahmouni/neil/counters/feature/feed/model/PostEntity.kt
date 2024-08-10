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

package dev.rahmouni.neil.counters.feature.feed.model

import dev.rahmouni.neil.counters.core.data.model.PostRawData
import dev.rahmouni.neil.counters.core.designsystem.rebased.PostType
import dev.rahmouni.neil.counters.core.designsystem.rebased.SharingScope
import java.time.LocalDateTime
import java.time.ZoneId

data class PostEntity(
    val uid: String,
    val userId: String?,
    val sharingScope: SharingScope,
    val location: String?,
    val timestamp: LocalDateTime,
    val categories: List<String> = emptyList(),
    val content: String?,
    val postType: PostType,
    val additionalInfos: List<Pair<String, Any>> = emptyList(),
)

fun PostRawData.toEntity(): PostEntity {
    if (uid == null) throw IllegalStateException("Attempted to convert a PostRawData with null uid to a PostEntity")

    return PostEntity(
        uid = uid!!,
        userId = userId,
        sharingScope = SharingScope.fromString(sharingScope),
        timestamp = timestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
        categories = categories,
        content = content,
        location = location,
        postType = PostType.fromString(postType),
        additionalInfos = additionalInfos.map { Pair(it.key, it.value) },
    )
}

