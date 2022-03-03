package rahmouni.neil.counters.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.IncrementValueType


@Database(entities = [Counter::class, Increment::class], version = 2)
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
                ).fallbackToDestructiveMigration().build()
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
    @ColumnInfo(name = "increment_type") val incrementType: IncrementType = IncrementType.VALUE,
    @ColumnInfo(name = "increment_value_type") val incrementValueType: IncrementValueType = IncrementValueType.VALUE,
    @ColumnInfo(name = "increment_value") val incrementValue: Int = 1,
)

data class CounterAugmented(
    @ColumnInfo(name = "uid") val uid: Int = 0,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "style") val style: CounterStyle = CounterStyle.DEFAULT,
    @ColumnInfo(name = "has_minus") val hasMinus: Boolean = false,
    @ColumnInfo(name = "increment_type") val incrementType: IncrementType = IncrementType.VALUE,
    @ColumnInfo(name = "increment_value_type") val incrementValueType: IncrementValueType = IncrementValueType.VALUE,
    @ColumnInfo(name = "increment_value") val incrementValue: Int = 1,
    @ColumnInfo(name = "count") val count: Int = 0,
    @ColumnInfo(name = "last_increment") val lastIncrement: Int = 1
)

@Entity(foreignKeys = [ForeignKey(entity = Counter::class,
    parentColumns = arrayOf("uid"),
    childColumns = arrayOf("counterID"),
    onDelete = ForeignKey.CASCADE)]
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

data class CounterWithIncrements(
    @Embedded val counter: CounterAugmented,
    @Relation(
        parentColumn = "uid",
        entityColumn = "counterID"
    )
    val increments: List<Increment>
)

@Dao
interface CountersListDao {
    @Query(
        "SELECT counter.*,sub.count,sub2.last_increment FROM counter LEFT JOIN (SELECT counterID, SUM(value) as count FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN " +
                "(SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid"
    )
    fun getAll(): Flow<List<CounterAugmented>>

    @Transaction
    @Query(
        "SELECT counter.*,sub.count,sub2.last_increment FROM counter LEFT JOIN (SELECT counterID, SUM(value) as count FROM increment GROUP BY counterID) as sub ON sub.counterID=counter.uid LEFT JOIN " +
                "(SELECT counterID,value as last_increment,Max(timestamp) as timestamp FROM increment GROUP BY counterID) as sub2 ON sub2.counterID=counter.uid WHERE counter.uid=:counterID"
    )
    fun getCounterWithIncrements(counterID: Int): Flow<CounterWithIncrements>

    @Query("SELECT * FROM counter WHERE uid=:counterID")
    fun getCounter(counterID: Int): Flow<Counter>

    @Query("INSERT INTO increment (value, counterID) VALUES(:value, :counterID)")
    suspend fun addIncrement(value: Int, counterID: Int): Long

    @Insert
    suspend fun addCounter(counter: Counter): Long

    @Delete
    suspend fun deleteIncrement(increment: Increment)

    @Query("DELETE FROM counter WHERE uid=:counterID")
    suspend fun deleteCounterById(counterID: Int)
}