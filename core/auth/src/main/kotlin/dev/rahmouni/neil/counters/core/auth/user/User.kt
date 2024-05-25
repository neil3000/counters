/*
 * Copyright 2024 Rahmouni Neïl
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

package dev.rahmouni.neil.counters.core.auth.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser

sealed interface Rn3User {
    data object LoggedOutUser : Rn3User
    data class SignedInUser(
        val uid: String,
        internal val displayName: String,
        internal val pfpUri: Uri?,
    ) : Rn3User

    fun getDisplayName(): String {
        return when (this) {
            LoggedOutUser -> "Not signed in" // TODO i18n
            is SignedInUser -> displayName
        }
    }
}

internal fun FirebaseUser?.toRn3User(): Rn3User {
    return when (this) {
        null -> LoggedOutUser
        else -> SignedInUser(
            uid = this.uid,
            displayName = this.displayName.toString(),
            pfpUri = this.photoUrl,
        )
    }
}
