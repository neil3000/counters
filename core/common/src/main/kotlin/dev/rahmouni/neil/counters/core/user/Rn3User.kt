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

package dev.rahmouni.neil.counters.core.user

import android.content.Context
import android.net.Uri
import dev.rahmouni.neil.counters.core.common.R.string

sealed interface Rn3User {

    data object Loading : Rn3User
    data object LoggedOutUser : Rn3User
    data class AnonymousUser(
        internal val uid: String,
        internal var address: AddressInfo,
        internal var phone: PhoneInfo?,
    ) : Rn3User
    data class SignedInUser(
        internal val uid: String,
        internal val displayName: String,
        val pfpUri: Uri?,
        internal val isAdmin: Boolean,
        internal val email: String,
        internal var address: AddressInfo,
        internal var phone: PhoneInfo?,
    ) : Rn3User

    fun getUid(): String = when (this) {
        is Loading -> "RahNeil_N3:Rn3User:Loading"
        is LoggedOutUser -> "RahNeil_N3:Rn3User:null"
        is AnonymousUser -> uid
        is SignedInUser -> uid
    }

    fun getDisplayName(context: Context): String {
        return when (this) {
            is SignedInUser -> displayName
            else -> context.getString(string.core_common_user_notSignedIn)
        }
    }

    fun getEmailAddress(): String? {
        return when (this) {
            is SignedInUser -> email
            else -> null
        }
    }

    fun getPhone(): PhoneInfo? {
        return when (this) {
            is SignedInUser -> phone
            is AnonymousUser -> phone
            else -> null
        }
    }

    fun getAddress(): AddressInfo? {
        return when (this) {
            is SignedInUser -> address
            is AnonymousUser -> address
            else -> null
        }
    }

    fun setPhone(phoneInfo: PhoneInfo?) {
        when (this) {
            is SignedInUser -> this.phone = phoneInfo
            is AnonymousUser -> this.phone = phoneInfo
            else -> {} // No operation or log an error if it's unexpected
        }
    }

    fun setAddress(addressInfo: AddressInfo) {
        when (this) {
            is SignedInUser -> this.address = addressInfo
            is AnonymousUser -> this.address = addressInfo
            else -> {} // No operation or log an error if it's unexpected
        }
    }

    fun isAdmin(): Boolean = this is SignedInUser && isAdmin
}
