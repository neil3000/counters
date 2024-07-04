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