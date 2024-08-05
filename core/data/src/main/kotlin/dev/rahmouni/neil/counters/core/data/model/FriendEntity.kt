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

package dev.rahmouni.neil.counters.core.data.model

import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.core.model.data.PhoneInfo
import dev.rahmouni.neil.counters.core.data.model.FriendRawData

data class FriendEntity(
    val uid: String,
    val name: String?,
    val email: String?,
    val phone: PhoneInfo?,
    val nearby: Boolean = false,
)

fun FriendRawData.toEntity(): FriendEntity {
        if (uid == null) throw IllegalStateException("Attempted to convert a FriendRawData with null uid to a FriendEntity")

        return FriendEntity(
            uid = uid,
            name = name,
            email = email,
            phone = PhoneInfo(
                code = Country.getCountryFromIso(phoneCode),
                number = phoneNumber
            ),
            nearby = nearby,
        )
    }

