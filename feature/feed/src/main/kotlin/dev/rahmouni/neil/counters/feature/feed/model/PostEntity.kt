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

