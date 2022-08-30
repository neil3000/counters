package rahmouni.neil.counters

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.ExerciseRepetitionsRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.records.metadata.Metadata
import rahmouni.neil.counters.counter_card.activity.health_connect.HealthConnectType
import java.time.ZonedDateTime

class HealthConnect {
    var client: HealthConnectClient? = null
    private var hasPermissions: Boolean = false
    private var isClientAvailable: Boolean = false
    var permissions: Set<Permission>? = null

    suspend fun initialize(context: Context) {
        if (hasSufficientSdk()) {
            isClientAvailable = HealthConnectClient.isAvailable(context)

            if (isClientAvailable) {
                permissions = setOf(
                    Permission.createWritePermission(ExerciseSessionRecord::class),
                    Permission.createWritePermission(ExerciseRepetitionsRecord::class),
                )

                client = HealthConnectClient.getOrCreate(context)
                hasPermissions = client?.permissionController?.getGrantedPermissions(permissions!!)
                    ?.containsAll(permissions!!) == true
            }
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

    suspend fun writeActivitySession(
        dateTime: ZonedDateTime,
        counterName: String,
        healthConnectType: HealthConnectType,
        count: Int,
        notes: String?
    ) {
        client?.insertRecords(
            listOf(
                /*ExerciseSessionRecord(
                    healthConnectType.activitySessionType,
                    counterName,
                    notes,
                    dateTime.toInstant().minusMillis((1000 * 60 * count).toLong()),
                    dateTime.offset,
                    dateTime.toInstant(),
                    dateTime.offset
                ),*/
                ExerciseRepetitionsRecord(
                    count.toLong(),
                    healthConnectType.repetitionsType,
                    dateTime.toInstant().minusMillis((1).toLong()),
                    dateTime.offset,
                    dateTime.toInstant(),
                    dateTime.offset,
                    metadata = Metadata(dataOrigin = DataOrigin("rahmouni.neil.counters"))
                )
            )
        )
    }
}