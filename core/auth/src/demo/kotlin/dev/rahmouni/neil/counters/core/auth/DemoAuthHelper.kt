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

        private val DEMO_ANONYMOUS_USER = AnonymousUser("demoAnonymousUser")
    }

    private val currentUser = MutableStateFlow<Rn3User>(LoggedOutUser)

    override suspend fun quickFirstSignIn(context: Context) {
        // currentUser.compareAndSet(currentUser.value, DEMO_SIGNEDIN_USER)
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
