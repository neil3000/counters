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
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialProviderConfigurationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dev.rahmouni.neil.counters.core.auth.user.Rn3User
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.AnonymousUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val WEB_CLIENT_ID = "743616795299-b7pstjgcvnq3sm77ia43vm66okovpejq.apps.googleusercontent.com"

/**
 * Implementation of `ConfigHelper` that fetches the value from the Firebase Remote Config backend.
 */
internal class FirebaseAuthHelper @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthHelper {

    private var claims = MutableStateFlow<Map<String, Any>>(emptyMap())

    override val authSignedIn: Boolean get() = firebaseAuth.currentUser != null

    init {
        firebaseAuth.addAuthStateListener { auth ->
            auth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                claims.compareAndSet(claims.value, it.claims)
            }
        }
    }

    override suspend fun signInWithCredentialManager(
        context: Context,
        filterByAuthorizedAccounts: Boolean,
    ) {
        try {
            firebaseAuth.signInWithCredential(
                GoogleAuthProvider.getCredential(
                    GoogleIdTokenCredential.createFrom(
                        CredentialManager
                            .create(context)
                            .getCredential(
                                context,
                                GetCredentialRequest.Builder()
                                    .setPreferImmediatelyAvailableCredentials(
                                        filterByAuthorizedAccounts,
                                    )
                                    .addCredentialOption(
                                        GetGoogleIdOption.Builder()
                                            .setFilterByAuthorizedAccounts(
                                                filterByAuthorizedAccounts,
                                            )
                                            .setServerClientId(WEB_CLIENT_ID)
                                            .setAutoSelectEnabled(filterByAuthorizedAccounts)
                                            .build(),
                                    ).build(),
                            )
                            .credential
                            .data,
                    ).idToken,
                    null,
                ),
            ).await()
        } catch (e: Exception) {
            when (e) {
                is NoCredentialException -> {
                    // It's ok to not have a credential already available
                    return
                }

                is GetCredentialCancellationException -> {
                    // It's ok to cancel the credential request
                    return
                }

                is GetCredentialProviderConfigurationException -> {
                    // Play services is not installed (or old version) AND is on an old Android version,
                    // or has a misconfig somewhere in the system stuff (custom ROM that removed the
                    // CredentialManager, ...)
                    if (filterByAuthorizedAccounts) return

                    // If user asked to login
                    // TODO show a modal to explain the issue to the user [RahNeil_N3:Z7pK6Dddr6flYbOYp2LXxpu7cBv3hlt0]
                    throw e
                }

                else -> throw e
            }
        }
    }

    override suspend fun signOut(context: Context) {
        val task = firebaseAuth.signInAnonymously().await()
        if (task.user != null) {
            CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
        }
    }

    override fun getUser(): Rn3User = firebaseAuth.currentUser.toRn3User(claims.value)

    override fun getUserFlow(): Flow<Rn3User> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.combine(claims) { user, claims ->
        user.toRn3User(claims)
    }

    private fun FirebaseUser?.toRn3User(claims: Map<String, Any>): Rn3User {
        if (this == null) throw IllegalAccessException("RahNeil_N3:ukLhXORwGrxysm2q6uVzYl4ZkcAbIyX8 / User isn't logged in yet")

        with(this) {
            if (isAnonymous) return AnonymousUser(uid)

            return SignedInUser(
                uid = uid,
                displayName = this.displayName.toString(),
                pfpUri = this.photoUrl,
                isAdmin = claims["role"] == "Admin",
            )
        }
    }
}
