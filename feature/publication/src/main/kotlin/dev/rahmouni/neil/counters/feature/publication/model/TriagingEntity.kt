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

package dev.rahmouni.neil.counters.feature.publication.model

import dev.rahmouni.neil.counters.core.data.model.TriagingRawData

data class TriagingEntity(
    val uid: String? = null,
    val text: String,
)

fun TriagingRawData.toEntity(): TriagingEntity {
    if (uid == null) throw IllegalStateException("Attempted to convert a TriagingRawData with null uid to a triagingEntity")

    return TriagingEntity(
        uid = uid,
        text = text ?: "",
    )
}

