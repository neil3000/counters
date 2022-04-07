package rahmouni.neil.counters.database

import android.text.format.DateFormat
import kotlinx.coroutines.flow.Flow
import java.util.*

class CountersListRepository(private val countersListDao: CountersListDao) {
    val allCounters: Flow<List<CounterAugmented>> = countersListDao.getAll()
    fun getCounterIncrements(counterID: Int): Flow<List<Increment>> = countersListDao.getCounterIncrements(counterID)
    fun getCounter(counterID: Int): Flow<CounterAugmented> = countersListDao.getCounter(counterID)
    fun getCounterIncrementGroups(
        counterID: Int,
        groupQuery1: String,
        groupQuery2: String
    ): Flow<List<IncrementGroup>> = countersListDao.getCounterIncrementGroups(counterID, groupQuery1, groupQuery2)

    suspend fun addIncrement(value: Int, counterID: Int) {
        countersListDao.addIncrement(
            Increment(
                value = value,
                counterID = counterID,
                timestamp = DateFormat.format("yyyy-MM-dd HH:mm:ss", Date()).toString()
            )
        )
    }

    fun testAddIncrement(increment: Increment) {
        countersListDao.testAddIncrement(increment)
    }

    suspend fun addCounter(counter: Counter) {
        countersListDao.addCounter(counter)
    }

    fun testAddCounter(counter: Counter): Long {
        return countersListDao.testAddCounter(counter)
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