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

package dev.rahmouni.neil.counters.core.auth

import android.content.Context
import dev.rahmouni.neil.counters.core.auth.user.Rn3User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AuthHelper] that does nothing or has default values, useful for tests and previews.
 */
@Singleton
class StubAuthHelper @Inject constructor() : AuthHelper {

    override suspend fun signInWithCredentialManager(
        context: Context,
        filterByAuthorizedAccounts: Boolean,
    ) {
        // Does nothing, we're in preview
    }

    override suspend fun signOut(context: Context) {
        // Does nothing, we're in preview
    }

    override fun getUser(): Rn3User = Rn3User.SignedInUser(
        displayName = "Android Preview",
        pfpUri = null,
    )

    override fun getUserFlow(viewModelScope: CoroutineScope): Flow<Rn3User> =
        throw NotImplementedError("Not available in Preview")
}
