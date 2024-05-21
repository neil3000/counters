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
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.rahmouni.neil.counters.core.auth.user.Rn3User
import dev.rahmouni.neil.counters.core.auth.user.toRn3User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val WEB_CLIENT_ID = "743616795299-b7pstjgcvnq3sm77ia43vm66okovpejq.apps.googleusercontent.com"

/**
 * Implementation of `ConfigHelper` that fetches the value from the Firebase Remote Config backend.
 */
internal class FirebaseAuthHelper @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthHelper {

    override suspend fun signInWithCredentialManager(
        context: Context,
        filterByAuthorizedAccounts: Boolean,
    ) {
        firebaseAuth.signInWithCredential(
            GoogleAuthProvider.getCredential(
                GoogleIdTokenCredential.createFrom(
                    CredentialManager
                        .create(context)
                        .getCredential(
                            context,
                            GetCredentialRequest.Builder()
                                .addCredentialOption(
                                    GetGoogleIdOption.Builder()
                                        .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
                                        .setServerClientId(WEB_CLIENT_ID)
                                        .build(),
                                ).build(),
                        )
                        .credential
                        .data,
                ).idToken,
                null,
            ),
        ).await()
    }

    override suspend fun signOut(context: Context) {
        firebaseAuth.signOut()
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
    }

    override fun getUser(): Rn3User = firebaseAuth.currentUser.toRn3User()

    override fun getUserFlow(viewModelScope: CoroutineScope): Flow<Rn3User> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser.toRn3User())
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }
}
