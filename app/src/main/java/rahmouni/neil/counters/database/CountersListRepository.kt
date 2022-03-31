package rahmouni.neil.counters.database

import android.util.Log
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class CountersListRepository(private val countersListDao: CountersListDao) {
    val allCounters: Flow<List<CounterAugmented>> = countersListDao.getAll()
    fun getCounterWithIncrements(counterID: Int): Flow<CounterWithIncrements> =
        countersListDao.getCounterWithIncrements(counterID)

    fun getCounterIncrementGroups(
        counterID: Int,
        groupQuery1: String,
        groupQuery2: String
    ): Flow<List<IncrementGroup>> = countersListDao.getCounterIncrementGroups(counterID, groupQuery1, groupQuery2)

    suspend fun addIncrement(value: Int, counterID: Int) {
        countersListDao.addIncrement(value, counterID)
    }

    suspend fun addCounter(counter: Counter) {
        countersListDao.addCounter(counter)
    }

    suspend fun deleteIncrement(increment: Increment) {
        countersListDao.deleteIncrement(increment)
    }

    suspend fun deleteCounterById(counterID: Int) {
        countersListDao.deleteCounterById(counterID)
    }

    suspend fun updateCounter(counter: Counter) {
        countersListDao.updateCounter(counter)
    }
}