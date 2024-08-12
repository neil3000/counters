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
        val phoneUtil = PhoneNumberUtil.getInstance()
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
                countryCode = Country.getCountryFromIso(phoneCode)?.phoneCode ?: 0
                nationalNumber = phoneNumber.toLong()
            }
        } else null,
        nearby = nearby,
        userId = userId,
    )
}

