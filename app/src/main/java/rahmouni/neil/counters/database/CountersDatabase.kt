package rahmouni.neil.counters.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.DeleteColumn
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.migration.AutoMigrationSpec
import kotlinx.coroutines.flow.Flow
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.R
import rahmouni.neil.counters.goals.GoalType
import rahmouni.neil.counters.goals.ResetType
import rahmouni.neil.counters.health_connect.HealthConnectAvailability
import rahmouni.neil.counters.health_connect.HealthConnectDataType
import rahmouni.neil.counters.health_connect.HealthConnectExerciseType
import rahmouni.neil.counters.health_connect.HealthConnectManager
import rahmouni.neil.counters.value_types.ValueType
import java.io.Serializable

@Database(
    entities = [Counter::class, Increment::class],
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(
            from = 6,
            to = 8,
            spec = CountersDatabase.Migration68::class
        ),
        AutoMigration(
            from = 8,
            to = 9,
            spec = CountersDatabase.Migration89::class
        ),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13),
    ],
    exportSchema = true,
    version = 13
)
abstract class CountersDatabase : RoomDatabase() {
    abstract fun countersListDao(): CountersListDao

    @DeleteColumn(tableName = "Counter", columnName = "increment_type")
    @DeleteColumn(tableName = "Counter", columnName = "increment_value_type")
    @RenameColumn(
        tableName = "Counter",
        fromColumnName = "increment_value",
        toColumnName = "button_plus_value"
    )
    @RenameColumn(
        tableName = "Counter",
        fromColumnName = "has_minus",
        toColumnName = "button_plus_enabled"
    )
    class Migration68 : AutoMigrationSpec

    @DeleteColumn(tableName = "Counter", columnName = "health_connect_type")
    class Migration89 : AutoMigrationSpec

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CountersDatabase? = null

        fun getInstance(context: Context): CountersDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountersDatabase::class.java,
                    "counters_database"
                ).fallbackToDestructiveMigrationFrom(1, 6, 7).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

@Entity
data class Counter(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "display_name") val displayName: String? = null,
    @ColumnInfo(name = "style") val style: CounterStyle = CounterStyle.DEFAULT,
    @ColumnInfo(name = "entry_value", defaultValue = "1") val entryValue: Int = 1,
    @ColumnInfo(
        name = "reset_type",
        defaultValue = "NEVER"
    ) val resetType: ResetType = ResetType.NEVER,
    @ColumnInfo(name = "reset_value", defaultValue = "0") val resetValue: Int = 0,
    @ColumnInfo(
        name = "health_connect_enabled",
        defaultValue = "false"
    ) val healthConnectEnabled: Boolean = false,
    @ColumnInfo(
        name = "health_connect_exercise_type",
        defaultValue = "BACK_EXTENSION"
    ) val healthConnectExerciseType: HealthConnectExerciseType = HealthConnectExerciseType.BACK_EXTENSION,
    @ColumnInfo(
        name = "health_connect_data_type",
        defaultValue = "REPETITIONS"
    ) val healthConnectDataType: HealthConnectDataType = HealthConnectDataType.REPETITIONS,
    @ColumnInfo(
        name = "value_type",
        defaultValue = "NUMBER"
    ) val valueType: ValueType = ValueType.NUMBER,

    @ColumnInfo(name = "button_plus_value", defaultValue = "1") val plusButtonValue: Int = 1,
    @ColumnInfo(
        name = "button_plus_enabled",
        defaultValue = "true"
    ) val plusButtonEnabled: Boolean = true,
    @ColumnInfo(name = "button_minus_value", defaultValue = "-1") val minusButtonValue: Int = -1,
    @ColumnInfo(
        name = "button_minus_enabled",
        defaultValue = "false"
    ) val minusButtonEnabled: Boolean = false,
    @ColumnInfo(
        name = "button_done_enabled",
        defaultValue = "true"
    ) val doneButtonEnabled: Boolean = true,
    @ColumnInfo(
        name = "button_notDone_enabled",
        defaultValue = "false"
    ) val notDoneButtonEnabled: Boolean = false,
    @ColumnInfo(name = "goal_value", defaultValue = "null") val goalValue: Int? = null,
    @ColumnInfo(name = "goal_enabled", defaultValue = "false") val goalEnabled: Boolean = false,
    @ColumnInfo(
        name = "goal_reset",
        defaultValue = "NEVER"
    ) val goalReset: ResetType? = null, //if null, follows the counter ResetValue
    @ColumnInfo(
        name = "suggestions_dismissed",
        defaultValue = "false"
    ) val dismissedSuggestions: Boolean = false,
    @ColumnInfo(
        name = "goal_type",
        defaultValue = "TIME_PERIOD"
    ) val goalType: GoalType = GoalType.TIME_PERIOD,
)

data class CounterAugmented(
    @ColumnInfo(name = "uid") val uid: Int = 0,
    @ColumnInfo(name = "display_name") private val displayName: String? = null,
    @ColumnInfo(name = "style") val style: CounterStyle = CounterStyle.DEFAULT,
    @ColumnInfo(name = "entry_value", defaultValue = "1") val entryValue: Int = 1,
    @ColumnInfo(
        name = "reset_type",
        defaultValue = "NEVER"
    ) val resetType: ResetType = ResetType.NEVER,
    @ColumnInfo(name = "reset_value", defaultValue = "0") val resetValue: Int = 0,
    @ColumnInfo(
        name = "health_connect_enabled",
        defaultValue = "false"
    ) val healthConnectEnabled: Boolean = false,
    @ColumnInfo(
        name = "health_connect_exercise_type",
        defaultValue = "BACK_EXTENSION"
    ) val healthConnectExerciseType: HealthConnectExerciseType = HealthConnectExerciseType.BACK_EXTENSION,
    @ColumnInfo(
        name = "health_connect_data_type",
        defaultValue = "REPETITIONS"
    ) val healthConnectDataType: HealthConnectDataType = HealthConnectDataType.REPETITIONS,
    @ColumnInfo(
        name = "value_type",
        defaultValue = "NUMBER"
    ) val valueType: ValueType = ValueType.NUMBER,

    @ColumnInfo(name = "button_plus_value", defaultValue = "1") val plusButtonValue: Int = 1,
    @ColumnInfo(
        name = "button_plus_enabled",
        defaultValue = "true"
    ) val plusButtonEnabled: Boolean = true,
    @ColumnInfo(name = "button_minus_value", defaultValue = "-1") val minusButtonValue: Int = -1,
    @ColumnInfo(
        name = "button_minus_enabled",
        defaultValue = "false"
    ) val minusButtonEnabled: Boolean = false,
    @ColumnInfo(
        name = "button_done_enabled",
        defaultValue = "true"
    ) val doneButtonEnabled: Boolean = true,
    @ColumnInfo(
        name = "button_notDone_enabled",
        defaultValue = "false"
    ) val notDoneButtonEnabled: Boolean = false,
    @ColumnInfo(name = "goal_value", defaultValue = "0") val goalValue: Int = 0,
    @ColumnInfo(
        name = "goal_enabled",
        defaultValue = "false"
    ) private val goalEnabled: Boolean = false,
    @ColumnInfo(name = "goal_reset", defaultValue = "NEVER") val goalReset: ResetType? = null,
    @ColumnInfo(
        name = "suggestions_dismissed",
        defaultValue = "false"
    ) val dismissedSuggestions: Boolean = false,
    @ColumnInfo(
        name = "goal_type",
        defaultValue = "TIME_PERIOD"
    ) val goalType: GoalType = GoalType.TIME_PERIOD,

    @ColumnInfo(name = "count_allTime") private val allTimeCount: Int = 0,
    @ColumnInfo(name = "count_day") private val dayCount: Int = 0,
    @ColumnInfo(name = "count_week") private val weekCount: Int = 0,
    @ColumnInfo(name = "count_month") private val monthCount: Int = 0,
    @ColumnInfo(name = "last_increment") val lastIncrement: Int = 1,
    @ColumnInfo(name = "top_increment") val topIncrement: Int = 0,
) {
    fun toCounter(): Counter {
        return Counter(
            uid = uid,
            displayName = displayName,
            style = style,
            entryValue = entryValue,
            resetType = resetType,
            resetValue = resetValue,
            healthConnectEnabled = healthConnectEnabled,
            healthConnectExerciseType = healthConnectExerciseType,
            healthConnectDataType = healthConnectDataType,
            valueType = valueType,

            plusButtonValue = plusButtonValue,
            plusButtonEnabled = plusButtonEnabled,
            minusButtonValue = minusButtonValue,
            minusButtonEnabled = minusButtonEnabled,
            doneButtonEnabled = doneButtonEnabled,
            notDoneButtonEnabled = notDoneButtonEnabled,

            goalValue = goalValue,
            goalEnabled = goalEnabled,
            goalReset = goalReset,
            goalType = goalType,

            dismissedSuggestions = dismissedSuggestions
        )
    }

    fun getCount(): Int {
        return getRawCount(resetType) + (if (resetType == ResetType.NEVER) 0 else resetValue)
    }

    fun getGoalProgress(): Float {
        return (if (goalType == GoalType.ENTRY) topIncrement else getRawCount(goalReset ?: resetType)).toFloat() / goalValue
    }

    fun getRawCount(resetType: ResetType): Int {
        return when (resetType) {
            ResetType.NEVER -> allTimeCount
            ResetType.DAY -> dayCount
            ResetType.WEEK -> weekCount
            ResetType.MONTH -> monthCount
        }
    }

    fun isGoalEnabled(): Boolean {
        return goalEnabled && goalValue > 0
    }

    fun isGoalSettingEnabled(): Boolean {
        return goalEnabled
    }

    fun getDisplayName(context: Context): String {
        return displayName ?: context.getString(R.string.counterDefaultName)
    }

    fun getDisplayNameOrNull(): String? {
        return displayName
    }

    fun shouldLogHealthConnect(healthConnectManager: HealthConnectManager): Boolean {
        return healthConnectManager.availability.value == HealthConnectAvailability.GRANTED && healthConnectEnabled
    }
}

@Entity(
    foreignKeys = [ForeignKey(
        entity = Counter::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("counterID"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Increment(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "counterID") val counterID: Int,
    @ColumnInfo(name = "value") val value: Int,
    @ColumnInfo(
        name = "timestamp",
        defaultValue = "CURRENT_TIMESTAMP"
    ) val timestamp: String = "CURRENT_TIMESTAMP",
    @ColumnInfo(name = "notes") val notes: String? = null
)

data class IncrementGroup(
    @ColumnInfo(name = "count") val count: Int = 0,
    @ColumnInfo(name = "date", defaultValue = "2000-01-01") val date: String = "2000-01-01",
    @ColumnInfo(name = "uids") val uids: String,
) : Serializable

@Dao
interface CountersListDao {
    /*@Query(
        "SELECT counter.*,sub.total_count,sub2.last_increment,sub3.count FROM counter LEFT JOIN (SELECT counterID, SUM(value) as total_count FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN (SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count FROM increment JOIN counter ON counter.uid=increment.counterID WHERE ((counter.reset_type='NEVER') OR (counter.reset_type='DAY' AND date(timestamp, 'start of day') >= date('now', 'localtime')) OR (counter.reset_type='WEEK' AND date(timestamp, ('weekday '||:weekday)) >= date('now', 'localtime')) OR (counter.reset_type='MONTH' AND date(timestamp, 'start of month') >= date('now'))) GROUP BY counterID) as sub3 ON sub3.counterID=counter.uid"
    )
    fun getAll(weekday: String): Flow<List<CounterAugmented>>*/

    /*@Query(
        "SELECT counter.*,sub.total_count,sub2.last_increment,sub3.count FROM counter LEFT JOIN (SELECT counterID, SUM(value) as total_count FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN (SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count FROM increment JOIN counter ON counter.uid=increment.counterID WHERE ((counter.reset_type='NEVER') OR (counter.reset_type='DAY' AND date(timestamp, 'start of day') >= date('now', 'localtime')) OR (counter.reset_type='WEEK' AND date(timestamp, ('weekday '||:weekday)) >= date('now', 'localtime')) OR (counter.reset_type='MONTH' AND date(timestamp, 'start of month') >= date('now'))) GROUP BY counterID) as sub3 ON sub3.counterID=counter.uid WHERE counter.uid=:counterID"
    )
    fun getCounter_old(counterID: Int, weekday: String): Flow<CounterAugmented>*/

    @Query(
        "SELECT counter.*,sub.count_allTime,sub.top_increment,sub2.last_increment,sub3.count_day,sub4.count_week,sub5.count_month FROM counter LEFT JOIN (SELECT counterID, SUM(value) as count_allTime, MAX(value) as top_increment FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN (SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count_day FROM increment WHERE date(timestamp, 'start of day') >= date('now', 'localtime') GROUP BY counterID) as sub3 ON sub3.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count_week FROM increment WHERE date(timestamp, ('weekday '||:weekday)) >= date('now', 'localtime') GROUP BY counterID) as sub4 ON sub4.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count_month FROM increment WHERE date(timestamp, 'start of month', '+1 month') >= date('now', 'localtime') GROUP BY counterID) as sub5 ON sub5.counterID=counter.uid"
    )
    fun getAll(weekday: String): Flow<List<CounterAugmented>>

    @Query(
        "SELECT counter.*,sub.count_allTime,sub.top_increment,sub2.last_increment,sub3.count_day,sub4.count_week,sub5.count_month FROM counter LEFT JOIN (SELECT counterID, SUM(value) as count_allTime, MAX(value) as top_increment FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN (SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count_day FROM increment WHERE date(timestamp, 'start of day') >= date('now', 'localtime') GROUP BY counterID) as sub3 ON sub3.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count_week FROM increment WHERE date(timestamp, ('weekday '||:weekday)) >= date('now', 'localtime') GROUP BY counterID) as sub4 ON sub4.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count_month FROM increment WHERE date(timestamp, 'start of month', '+1 month') >= date('now', 'localtime') GROUP BY counterID) as sub5 ON sub5.counterID=counter.uid WHERE counter.uid=:counterID"
    )
    fun getCounter(counterID: Int, weekday: String): Flow<CounterAugmented>

    @Query(
        "SELECT * FROM increment WHERE counterID=:counterID ORDER BY timestamp DESC"
    )
    fun getCounterIncrements(counterID: Int): Flow<List<Increment>>

    @Query(
        "SELECT SUM(value) as count, date(timestamp, :groupQuery1) as date, GROUP_CONCAT(uid, ',') AS uids FROM increment WHERE counterID=:counterID GROUP BY date(timestamp, :groupQuery1) ORDER BY timestamp DESC"
    )
    fun getCounterIncrementGroups(
        counterID: Int,
        groupQuery1: String
    ): Flow<List<IncrementGroup>>

    @Insert
    suspend fun addIncrement(increment: Increment)

    @Insert
    fun testAddIncrement(increment: Increment)

    @Insert
    suspend fun addCounter(counter: Counter): Long

    @Insert
    fun testAddCounter(counter: Counter): Long

    @Delete
    suspend fun deleteIncrement(increment: Increment)

    @Query("DELETE FROM counter WHERE uid=:counterID")
    suspend fun deleteCounterById(counterID: Int)

    @Update
    suspend fun updateCounter(counter: Counter)
}