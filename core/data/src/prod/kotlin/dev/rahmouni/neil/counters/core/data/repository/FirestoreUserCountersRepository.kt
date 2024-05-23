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

package dev.rahmouni.neil.counters.core.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.rahmouni.neil.counters.core.data.model.UserCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class FirestoreUserCountersRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : UserCountersRepository {

    override val userCounters: Flow<List<UserCounter>> = firebaseAuth.currentUser.let {
        if (it != null) Firebase.firestore.collection(it.uid).dataObjects() else emptyFlow()
    }

    override fun createUserCounter(title: String) {
        firebaseAuth.currentUser.let {
            if (it != null) {
                Firebase.firestore.collection(it.uid).add(
                    hashMapOf(
                        "title" to title,
                    ),
                ).addOnCompleteListener {
                    Log.d("RahNeil_N3", it.result.toString())
                }
            } else {
                Log.d("RahNeil_N3", "ERROR")
            }
        }
    }
}
