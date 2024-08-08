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

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.rahmouni.neil.counters.core.model.data.Country

data class FriendEntity(
    val uid: String,
    val name: String?,
    val email: String?,
    val phone: PhoneNumber?,
    val nearby: Boolean = false,
    val userId: String?,
) {
    fun display(): String {
        return name?.takeIf { it.isNotEmpty() } ?: formatPhone() ?: ""
    }

    fun formatPhone(): String? {
        val phoneUtil = PhoneNumberUtil.getInstance();
        return if (phone != null) phoneUtil.format(phone, INTERNATIONAL) else null
    }
}

fun FriendRawData.toEntity(): FriendEntity {
    if (uid == null) throw IllegalStateException("Attempted to convert a FriendRawData with null uid to a FriendEntity")

    return FriendEntity(
        uid = uid,
        name = name,
        email = email,
        phone = if (phoneNumber != null && phoneCode != null) {
            PhoneNumber().apply {
                countryCode = Country.getCountryFromIso(phoneCode)!!.phoneCode
                nationalNumber = phoneNumber.toLong()
            }
        } else null,
        nearby = nearby,
        userId = userId,
    )
}

