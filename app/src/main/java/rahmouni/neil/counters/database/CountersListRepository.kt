package rahmouni.neil.counters.database

import android.text.format.DateFormat
import kotlinx.coroutines.flow.Flow
import rahmouni.neil.counters.prefs
import java.util.*

class CountersListRepository(private val countersListDao: CountersListDao) {
    private val weekday = prefs.startWeekDay.groupQuery
        ?: ((Calendar.getInstance().firstDayOfWeek + 5) % 7).toString()

    val allCounters: Flow<List<CounterAugmented>> = countersListDao.getAll(weekday)
    fun getCounterIncrements(counterID: Int): Flow<List<Increment>> =
        countersListDao.getCounterIncrements(counterID)

    fun getCounter(counterID: Int): Flow<CounterAugmented> =
        countersListDao.getCounter(counterID, weekday)

    fun getCounterIncrementGroups(
        counterID: Int,
        groupQuery1: String,
    ): Flow<List<IncrementGroup>> {
        return countersListDao.getCounterIncrementGroups(
            counterID,
            groupQuery1.replaceFirst("%d", weekday)
        )
    }

    suspend fun addIncrement(value: Int, counterID: Int, date: String?, notes: String?) {
        countersListDao.addIncrement(
            Increment(
                value = value,
                counterID = counterID,
                timestamp = date ?: DateFormat.format("yyyy-MM-dd HH:mm:ss", Date()).toString(),
                notes = notes
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