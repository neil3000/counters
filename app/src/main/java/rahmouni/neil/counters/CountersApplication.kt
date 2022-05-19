package rahmouni.neil.counters

import android.app.Application
import androidx.health.connect.client.HealthConnectClient
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.database.CountersDatabase
import rahmouni.neil.counters.database.CountersListRepository
import rahmouni.neil.counters.settings.Prefs

val prefs: Prefs by lazy {
    CountersApplication.prefs!!
}

class CountersApplication : Application() {
    private val database by lazy { CountersDatabase.getInstance(this) }
    val countersListRepository by lazy { CountersListRepository(database.countersListDao()) }
    var healthConnectClient: HealthConnectClient? = null

    companion object {
        var prefs: Prefs? = null
        var analytics: FirebaseAnalytics? = null
        var crashlytics: FirebaseCrashlytics? = null

        lateinit var instance: CountersApplication
            private set
    }

    override fun onCreate() {
        instance = this
        prefs = Prefs(applicationContext)


        FirebaseApp.initializeApp(this)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setDefaultsAsync(if (BuildConfig.DEBUG) R.xml.remote_config_debug else R.xml.remote_config_defaults)
        if (!BuildConfig.DEBUG) remoteConfig.fetchAndActivate()

        crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics!!.setCrashlyticsCollectionEnabled(rahmouni.neil.counters.prefs.crashlyticsEnabled && !BuildConfig.DEBUG)

        analytics = Firebase.analytics
        analytics?.setAnalyticsCollectionEnabled(rahmouni.neil.counters.prefs.analyticsEnabled && !BuildConfig.DEBUG)

        if (BuildConfig.FLAVOR == "minApi27") {
            if (HealthConnectClient.isAvailable(this)) {
                healthConnectClient = HealthConnectClient.getOrCreate(this)
            }
        }

        super.onCreate()
    }
}