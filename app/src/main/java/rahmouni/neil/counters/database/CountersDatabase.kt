package rahmouni.neil.counters.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.IncrementValueType
import rahmouni.neil.counters.ResetType
import java.io.Serializable

@Database(
    entities = [Counter::class, Increment::class],
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ],
    exportSchema = true,
    version = 4
)
abstract class CountersDatabase : RoomDatabase() {
    abstract fun countersListDao(): CountersListDao

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
                ).fallbackToDestructiveMigrationFrom(1).build()
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
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "style") val style: CounterStyle = CounterStyle.DEFAULT,
    @ColumnInfo(name = "has_minus") val hasMinus: Boolean = false,
    @ColumnInfo(name = "increment_type") val incrementType: IncrementType = IncrementType.ASK_EVERY_TIME,
    @ColumnInfo(name = "increment_value_type") val incrementValueType: IncrementValueType = IncrementValueType.VALUE,
    @ColumnInfo(name = "increment_value") val incrementValue: Int = 1,
    @ColumnInfo(
        name = "reset_type",
        defaultValue = "NEVER"
    ) val resetType: ResetType = ResetType.NEVER,
    @ColumnInfo(
        name = "reset_value",
        defaultValue = "0"
    ) val resetValue: Int = 0,
)

data class CounterAugmented(
    @ColumnInfo(name = "uid") val uid: Int = 0,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "style") val style: CounterStyle = CounterStyle.DEFAULT,
    @ColumnInfo(name = "has_minus") val hasMinus: Boolean = false,
    @ColumnInfo(name = "increment_type") val incrementType: IncrementType = IncrementType.ASK_EVERY_TIME,
    @ColumnInfo(name = "increment_value_type") val incrementValueType: IncrementValueType = IncrementValueType.VALUE,
    @ColumnInfo(name = "increment_value") val incrementValue: Int = 1,
    @ColumnInfo(
        name = "reset_type",
        defaultValue = "NEVER"
    ) val resetType: ResetType = ResetType.NEVER,
    @ColumnInfo(
        name = "reset_value",
        defaultValue = "0"
    ) val resetValue: Int = 0,

    @ColumnInfo(name = "total_count") val totalCount: Int = 0,
    @ColumnInfo(name = "last_increment") val lastIncrement: Int = 1,
    @ColumnInfo(name = "count") val count: Int = 0
) {
    fun toCounter(): Counter {
        return Counter(
            uid = uid,
            displayName = displayName,
            style = style,
            hasMinus = hasMinus,
            incrementType = incrementType,
            incrementValueType = incrementValueType,
            incrementValue = incrementValue,
            resetType = resetType,
            resetValue = resetValue
        )
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
    ) val timestamp: String = "CURRENT_TIMESTAMP"
)

data class IncrementGroup(
    @ColumnInfo(name = "count") val count: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "uids") val uids: String,
) : Serializable

@Dao
interface CountersListDao {
    @Query(
        "SELECT counter.*,sub.total_count,sub2.last_increment,sub3.count FROM counter LEFT JOIN (SELECT counterID, SUM(value) as total_count FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN (SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count FROM increment  JOIN counter ON counter.uid=increment.counterID WHERE ((counter.reset_type='NEVER') OR (counter.reset_type='DAY' AND date(timestamp, 'start of day') >= date('now', 'localtime')) OR (counter.reset_type='WEEK' AND date(timestamp, ('weekday '||:weekday)) >= date('now', 'localtime')) OR (counter.reset_type='MONTH' AND date(timestamp, 'start of month') >= date('now', 'localtime'))) GROUP BY counterID) as sub3 ON sub3.counterID=counter.uid"
    )
    fun getAll(weekday: String): Flow<List<CounterAugmented>>

    @Query(
        "SELECT counter.*,sub.total_count,sub2.last_increment,sub3.count FROM counter LEFT JOIN (SELECT counterID, SUM(value) as total_count FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN (SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid LEFT JOIN (SELECT counterID, SUM(value) as count FROM increment  JOIN counter ON counter.uid=increment.counterID WHERE ((counter.reset_type='NEVER') OR (counter.reset_type='DAY' AND date(timestamp, 'start of day') >= date('now', 'localtime')) OR (counter.reset_type='WEEK' AND date(timestamp, ('weekday '||:weekday)) >= date('now', 'localtime')) OR (counter.reset_type='MONTH' AND date(timestamp, 'start of month') >= date('now', 'localtime'))) GROUP BY counterID) as sub3 ON sub3.counterID=counter.uid WHERE counter.uid=:counterID"
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