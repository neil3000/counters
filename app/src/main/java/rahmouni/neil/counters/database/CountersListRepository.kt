package rahmouni.neil.counters.database

import kotlinx.coroutines.flow.Flow

class CountersListRepository(private val countersListDao: CountersListDao) {
    val allCounters: Flow<List<CounterAugmented>> = countersListDao.getAll()
    fun getCounterWithIncrements(counterID: Int): Flow<CounterWithIncrements> = countersListDao.getCounterWithIncrements(counterID)

    suspend fun addIncrement(value: Int, counterID: Int) {
        countersListDao.addIncrement(value, counterID)
    }

    suspend fun addCounter(counter: Counter) {
        countersListDao.addCounter(counter)
    }

    suspend fun deleteIncrement(increment: Increment) {
        countersListDao.deleteIncrement(increment)
    }
}