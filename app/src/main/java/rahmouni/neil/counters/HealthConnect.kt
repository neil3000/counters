package rahmouni.neil.counters

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.metadata.DataOrigin
import androidx.health.connect.client.metadata.Metadata
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.ActivitySession
import androidx.health.connect.client.records.Repetitions
import rahmouni.neil.counters.counter_card.activity.health_connect.HealthConnectType
import java.time.ZonedDateTime
import java.util.*

class HealthConnect {
    private var client: HealthConnectClient? = null
    private var hasPermissions: Boolean = false
    private var isClientAvailable: Boolean = false
    val permissions = setOf(Permission.createWritePermission(Repetitions::class), Permission.createWritePermission(ActivitySession::class))

    suspend fun initialize(context: Context) {
        isClientAvailable = HealthConnectClient.isAvailable(context)

        if (hasSufficientSdk() && isClientAvailable()) {
            client = HealthConnectClient.getOrCreate(context)
            hasPermissions = client?.permissionController?.getGrantedPermissions(permissions)
                ?.containsAll(permissions) == true
        }
    }

    /**
     * Function to check if the integration is available, meaning the user
     * is on the right flavor, has the Health Connect app installed and
     * has the right permissions.
     *
     * The value might be cached, if you want to force refresh the value,
     * you need to use initialize()
     *
     * @return whether or not the integration is available.
     */
    fun isAvailable(): Boolean {
        return hasSufficientSdk() && isClientAvailable() && hasPermissions()
    }

    fun isClientAvailable(): Boolean {
        return isClientAvailable
    }

    fun hasSufficientSdk(): Boolean {
        return BuildConfig.FLAVOR == "minApi27"
    }

    fun hasPermissions(): Boolean {
        return hasPermissions
    }

    suspend fun writeActivitySession(dateTime: ZonedDateTime, counterName: String, healthConnectType: HealthConnectType, count: Int) {
        client?.insertRecords(
            listOf(
                ActivitySession(
                    healthConnectType.activitySessionType,
                    counterName,
                    null,
                    dateTime.toInstant().minusMillis((1000 * 60 * count).toLong()),
                    dateTime.offset,
                    dateTime.toInstant(),
                    dateTime.offset
                ),
                Repetitions(
                    count.toLong(),
                    healthConnectType.repetitionsType,
                    dateTime.toInstant().minusMillis((1000 * 60 * count).toLong()),
                    dateTime.offset,
                    dateTime.toInstant(),
                    dateTime.offset,
                    metadata = Metadata(
                        dataOrigin = DataOrigin("rahmouni.neil.counters"),
                        lastModifiedTime = Date().toInstant()
                    )
                )
            )
        )
    }
}