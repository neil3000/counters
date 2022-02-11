package rahmouni.neil.counters

import androidx.room.*

@Database(entities = [Counter::class, Increment::class], version = 1)
abstract class CountersDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao
}

@Entity
data class Counter(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "style") val style: CounterStyle = CounterStyle.DEFAULT,
    @ColumnInfo(name = "has_minus") val hasMinus: Boolean = false,
    @ColumnInfo(name = "increment_type") val incrementType: IncrementType = IncrementType.VALUE,
    @ColumnInfo(name = "increment_value") val incrementValue: Int = 1,
)

@Entity
data class Increment(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "counterID") val counterID: Int,
    @ColumnInfo(name = "value") val value: Int
)

@Dao
interface CounterDao {
    @Query("SELECT * FROM counter")
    suspend fun getAll(): List<Counter>

    @Insert
    suspend fun addCounter(counter: Counter): Long

    @Insert
    suspend fun addIncrement(increment: Increment): Long

    @Query("SELECT SUM(value) AS value FROM increment WHERE counterID=:counterID")
    suspend fun getCount(counterID: Int): Int
}