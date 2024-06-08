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
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.auth.R

sealed interface Rn3User {

    val uid: String

    data class AnonymousUser(override val uid: String) : Rn3User
    data class SignedInUser(
        override val uid: String,
        internal val displayName: String,
        internal val pfpUri: Uri?,
        internal val isAdmin: Boolean,
    ) : Rn3User

    @Composable
    fun getDisplayName(): String {
        return when (this) {
            is AnonymousUser -> stringResource(R.string.core_auth_user_notSignedIn)
            is SignedInUser -> displayName
        }
    }

    fun isAdmin(): Boolean = this is SignedInUser && isAdmin
}
