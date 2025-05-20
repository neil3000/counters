package rahmouni.neil.counters

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.installations.installations
import com.google.firebase.remoteconfig.remoteConfig
import rahmouni.neil.counters.database.CountersDatabase
import rahmouni.neil.counters.database.CountersListRepository
import rahmouni.neil.counters.settings.Prefs

var prefs: Prefs = CountersApplication.prefs!!

class CountersApplication : Application() {
    private val database by lazy { CountersDatabase.getInstance(this) }
    val countersListRepository by lazy { CountersListRepository(database.countersListDao()) }

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

        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(if (BuildConfig.DEBUG) R.xml.remote_config_debug else R.xml.remote_config_defaults)
        if (!BuildConfig.DEBUG) remoteConfig.fetchAndActivate()

        crashlytics = Firebase.crashlytics
        crashlytics!!.isCrashlyticsCollectionEnabled =
            rahmouni.neil.counters.prefs.crashlyticsEnabled && !BuildConfig.DEBUG

        analytics = Firebase.analytics
        analytics?.setAnalyticsCollectionEnabled(rahmouni.neil.counters.prefs.analyticsEnabled && !BuildConfig.DEBUG)

        Firebase.installations.id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseInstallationID = task.result
            }
        }

        prefs!!.tipsStatus += 3

        super.onCreate()
    }
}