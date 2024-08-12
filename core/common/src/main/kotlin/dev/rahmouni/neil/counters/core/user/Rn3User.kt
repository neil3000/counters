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

package dev.rahmouni.neil.counters.core.user

import android.content.Context
import android.net.Uri
import dev.rahmouni.neil.counters.core.common.R.string

sealed interface Rn3User {

    data object Loading : Rn3User
    data object LoggedOutUser : Rn3User
    data class AnonymousUser(
        internal val uid: String,
    ) : Rn3User

    data class SignedInUser(
        internal val uid: String,
        internal val displayName: String,
        val pfpUri: Uri?,
        internal val isAdmin: Boolean,
        internal val email: String,
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

    fun isAdmin(): Boolean = this is SignedInUser && isAdmin
}
