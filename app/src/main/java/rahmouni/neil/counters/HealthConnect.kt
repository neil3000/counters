package rahmouni.neil.counters

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.metadata.DataOrigin
import androidx.health.connect.client.metadata.Metadata
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.Repetitions
import java.util.*

class HealthConnect {
    private var client: HealthConnectClient? = null
    private var hasPermissions: Boolean = false
    val permissions = Permission.createWritePermission(Repetitions::class)

    suspend fun initialize(context: Context) {
        if (hasSufficientSdk() && isClientAvailable(context)) client =
            HealthConnectClient.getOrCreate(context)

        hasPermissions = client?.permissionController?.getGrantedPermissions(setOf(permissions))
            ?.contains(permissions) == true
    }

    fun isAvailable(context: Context): Boolean {
        return hasSufficientSdk() && isClientAvailable(context) && hasPermissions()
    }

    fun isClientAvailable(context: Context): Boolean {
        return HealthConnectClient.isAvailable(context)
    }

    fun hasSufficientSdk(): Boolean {
        return BuildConfig.FLAVOR == "minApi27"
    }

    fun hasPermissions(): Boolean {
        return hasPermissions
    }

    suspend fun writeActivitySession() {
        if (hasPermissions()) {
            client?.insertRecords(
                listOf(
                    Repetitions(
                        120,
                        Repetitions.ActivityType.SQUAT,
                        Date().toInstant().minusMillis(1000 * 60 * 30),
                        null,
                        endTime = Date().toInstant(),
                        null,
                        metadata = Metadata(
                            dataOrigin = DataOrigin("rahmouni.neil.counters"),
                            lastModifiedTime = Date().toInstant()
                        )
                    )
                )
            )
        } else {

        }
    }
}