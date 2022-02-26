package rahmouni.neil.counters

import android.app.Application
import rahmouni.neil.counters.database.CountersDatabase
import rahmouni.neil.counters.database.CountersListRepository

const val DEBUG_MODE = false

class CountersApplication : Application() {
    private val database by lazy { CountersDatabase.getInstance(this) }
    val countersListRepository by lazy { CountersListRepository(database.countersListDao()) }
}