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

package dev.rahmouni.neil.counters.core.auth

import android.content.Context
import androidx.core.net.toUri
import dev.rahmouni.neil.counters.core.user.Rn3User
import dev.rahmouni.neil.counters.core.user.Rn3User.AnonymousUser
import dev.rahmouni.neil.counters.core.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AuthHelper] that simulates real sign in flows and has default demo values, but doesn't interact with the backend.
 */
@Singleton
internal class DemoAuthHelper @Inject constructor() : AuthHelper {

    companion object {
        private val DEMO_SIGNEDIN_USER = SignedInUser(
            uid = "demoSignedInUser",
            displayName = "Neïl (demo)",
            pfpUri = "https://firebasestorage.googleapis.com/v0/b/rahneil-n3-counters.appspot.com/o/demo%2Fpfp.jpg?alt=media".toUri(),
            isAdmin = false,
            email = "demo@rahmouni.dev",
        )

        private val DEMO_ANONYMOUS_USER = AnonymousUser(
            "demoAnonymousUser",
        )
    }

    private val currentUser = MutableStateFlow<Rn3User>(DEMO_SIGNEDIN_USER)

    override suspend fun quickFirstSignIn(context: Context) {
        currentUser.compareAndSet(currentUser.value, DEMO_SIGNEDIN_USER)
    }

    override suspend fun signIn(context: Context, anonymously: Boolean) {
        currentUser.compareAndSet(
            currentUser.value,
            if (anonymously) DEMO_ANONYMOUS_USER else DEMO_SIGNEDIN_USER,
        )
    }

    override suspend fun signOut(context: Context) {
        currentUser.compareAndSet(currentUser.value, LoggedOutUser)
    }

    override fun getUser(): Rn3User = currentUser.value

    override fun getUserFlow(): Flow<Rn3User> = currentUser
}
