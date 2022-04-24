package rahmouni.neil.counters

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import rahmouni.neil.counters.database.CountersDatabase
import rahmouni.neil.counters.database.CountersListRepository
import rahmouni.neil.counters.settings.Prefs

val prefs: Prefs by lazy {
    CountersApplication.prefs!!
}

class CountersApplication : Application() {
    private val database by lazy { CountersDatabase.getInstance(this) }
    val countersListRepository by lazy { CountersListRepository(database.countersListDao()) }

    companion object {
        var prefs: Prefs? = null
        var analytics: FirebaseAnalytics? = null

        lateinit var instance: CountersApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = Prefs(applicationContext)
        analytics = Firebase.analytics
    }
}