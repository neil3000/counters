package rahmouni.neil.counters

import android.app.Application
import rahmouni.neil.counters.database.CountersDatabase
import rahmouni.neil.counters.database.CountersListRepository

val prefs: Prefs by lazy {
    CountersApplication.prefs!!
}

class CountersApplication : Application() {
    private val database by lazy { CountersDatabase.getInstance(this) }
    val countersListRepository by lazy { CountersListRepository(database.countersListDao()) }

    companion object {
        var prefs: Prefs? = null
        lateinit var instance: CountersApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = Prefs(applicationContext)
    }}