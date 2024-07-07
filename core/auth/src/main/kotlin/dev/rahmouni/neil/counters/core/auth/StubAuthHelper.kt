/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.core.auth

import android.content.Context
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AuthHelper] that does nothing or has default values, useful for tests and previews.
 */
@Singleton
class StubAuthHelper @Inject constructor() : AuthHelper {

    override suspend fun quickFirstSignIn(context: Context) =
        throw NotImplementedError("Not available in Preview")

    override suspend fun signIn(context: Context, anonymously: Boolean) =
        throw NotImplementedError("Not available in Preview")

    override suspend fun signOut(context: Context) =
        throw NotImplementedError("Not available in Preview")

    override fun getUser(): dev.rahmouni.neil.counters.core.user.Rn3User =
        dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser(
            uid = "androidPreviewId",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
        )

    override fun getUserFlow(): Flow<dev.rahmouni.neil.counters.core.user.Rn3User> =
        throw NotImplementedError("Not available in Preview")
}
