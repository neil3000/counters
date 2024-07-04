package dev.rahmouni.neil.counters.core.data.repository.countersData

import dev.rahmouni.neil.counters.core.data.model.CounterRawData
import dev.rahmouni.neil.counters.core.data.model.IncrementRawData
import kotlinx.coroutines.flow.Flow

interface CountersDataRepository {

    val userCounters: Flow<List<CounterRawData>>

    fun createCounter(counterRawData: CounterRawData)
    fun createIncrement(counterUid: String, incrementRawData: IncrementRawData)
}