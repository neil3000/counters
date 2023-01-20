package rahmouni.neil.counters

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.database.CountersDatabase
import rahmouni.neil.counters.database.CountersListRepository
import rahmouni.neil.counters.health_connect.HealthConnectManager
import rahmouni.neil.counters.settings.Prefs

var prefs: Prefs = CountersApplication.prefs!!

class CountersApplication : Application() {
    private val database by lazy { CountersDatabase.getInstance(this) }
    val countersListRepository by lazy { CountersListRepository(database.countersListDao()) }
    val healthConnectManager by lazy { HealthConnectManager(this, kotlinx.coroutines.MainScope()) }

    companion object {
        var prefs: Prefs? = null
        var analytics: FirebaseAnalytics? = null
        var crashlytics: FirebaseCrashlytics? = null
        var firebaseInstallationID: String? = null

        lateinit var instance: CountersApplication
            private set
    }

    override fun onCreate() {
        instance = this
        prefs = Prefs(applicationContext)

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

        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseInstallationID = task.result
            }
        }

        prefs!!.tipsStatus = prefs!!.tipsStatus + 3

        super.onCreate()
    }
}