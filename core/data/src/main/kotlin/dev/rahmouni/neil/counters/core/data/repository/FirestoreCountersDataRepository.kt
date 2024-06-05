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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.rahmouni.neil.counters.core.data.model.CounterRawData
import dev.rahmouni.neil.counters.core.data.model.CounterRawDataDefaults
import dev.rahmouni.neil.counters.core.data.model.IncrementRawData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreCountersDataRepository @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : CountersDataRepository {

    override val lastUserUid: Flow<String?> =
        userDataRepository.userData.map { it.lastUserUid }

    override val userCounters: Flow<List<CounterRawData>> =
        userDataRepository.userData.transformLatest { user ->
            if (user.lastUserUid != null) {
                emitAll(
                    try {
                        Firebase.firestore
                            .collection("counters")
                            .whereEqualTo(
                                CounterRawData::ownerUserUid.name,
                                user.lastUserUid ?: CounterRawDataDefaults.ownerUserUid,
                            )
                            .dataObjects<CounterRawData>()
                    } catch (e: Exception) {
                        throw e
                        // TODO Feedback error
                    },
                )
            }
        }

    override suspend fun createCounter(counterRawData: CounterRawData) {

        val test = counterRawData.copy(
            ownerUserUid = userDataRepository.userData.last().lastUserUid
                ?: CounterRawDataDefaults.ownerUserUid,
        ).toMap()
        Log.d("test", test.toString())

        Firebase.firestore
            .collection("counters")
            .add(test).addOnCompleteListener {
                Log.d("test", it.result.toString())
            }.await()

        /*coroutineScope {
            userDataRepository.userData.stateIn(this).value.let { userData ->
                Log.d("RahNeil_N3", "B1: ${userData.lastUserUid}")
                try {
                    Log.d(
                        "RahNeil_N3",
                        counterRawData.copy(
                            ownerUserUid = userData.lastUserUid
                                ?: CounterRawDataDefaults.ownerUserUid,
                        ).toMap().toString(),
                    )
                    Firebase.firestore
                        .collection("counters")
                        .add(
                            counterRawData.copy(
                                ownerUserUid = userData.lastUserUid
                                    ?: CounterRawDataDefaults.ownerUserUid,
                            ).toMap(),
                        ).await()
                } catch (e: Exception) {
                    throw e
                }
            }
        }*/
    }

    override fun createIncrement(
        counterUid: String,
        incrementRawData: IncrementRawData,
    ) {
        try {
            Firebase.firestore
                .collection("counters")
                .document(counterUid)
                .let {
                    it.update(
                        CounterRawData::currentValue.name,
                        FieldValue.increment(incrementRawData.value),
                    )
                    it.collection("increments").add(incrementRawData)
                }
        } catch (e: Exception) {
            throw e
        }
    }
}
