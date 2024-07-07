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

package dev.rahmouni.neil.counters.core.data.repository.countersData

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.model.CounterRawData
import dev.rahmouni.neil.counters.core.data.model.IncrementRawData
import dev.rahmouni.neil.counters.core.data.repository.logCreatedCounter
import dev.rahmouni.neil.counters.core.data.repository.logCreatedIncrement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class FirestoreCountersDataRepository @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val authHelper: AuthHelper,
) : CountersDataRepository {

    override val userCounters: Flow<List<CounterRawData>> =
        authHelper.getUserFlow().transformLatest { user ->
            emitAll(
                try {
                    Firebase.firestore
                        .collection("counters")
                        .whereEqualTo(
                            CounterRawData::ownerUserUid.name,
                            user.getUid(),
                        )
                        .dataObjects<CounterRawData>()
                } catch (e: Exception) {
                    throw e
                    // TODO Feedback error
                },
            )
        }

    override fun createCounter(counterRawData: CounterRawData) {
        Firebase.firestore
            .collection("counters")
            .add(
                counterRawData.copy(
                    ownerUserUid = authHelper.getUser().getUid(),
                ),
            )

        analyticsHelper.logCreatedCounter()
    }

    override fun createIncrement(
        counterUid: String,
        incrementRawData: IncrementRawData,
    ) {
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

        analyticsHelper.logCreatedIncrement()
    }
}
