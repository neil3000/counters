package rahmouni.neil.counters

import android.content.Context
import androidx.health.connect.client.HealthConnectClient

class HealthConnect {
    private var client: HealthConnectClient? = null

    fun initialize(context: Context) {
        client = HealthConnectClient.getOrCreate(context)
    }

    fun isAvailable(context: Context): Boolean {
        return hasSufficientSdk() && isClientAvailable(context)
    }

    fun isClientAvailable(context: Context): Boolean {
        return HealthConnectClient.isAvailable(context)
    }

    fun hasSufficientSdk(): Boolean {
        return BuildConfig.FLAVOR == "minApi27"
    }
}