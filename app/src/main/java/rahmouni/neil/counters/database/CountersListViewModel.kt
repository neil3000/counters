package rahmouni.neil.counters.database

import androidx.lifecycle.*
import com.google.firebase.analytics.ktx.logEvent
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.healthConnect

class CountersListViewModel(private val repository: CountersListRepository) : ViewModel() {
    val allCounters: LiveData<List<CounterAugmented>> = repository.allCounters.asLiveData()
    fun getCounterIncrements(counterID: Int): LiveData<List<Increment>> =
        repository.getCounterIncrements(counterID).asLiveData()

    fun getCounter(counterID: Int): LiveData<CounterAugmented> =
        repository.getCounter(counterID).asLiveData()

    fun getCounterIncrementGroups(
        counterID: Int,
        resetType: ResetType
    ): LiveData<List<IncrementGroup>> =
        repository.getCounterIncrementGroups(
            counterID,
            resetType.entriesGroup1!!
        ).asLiveData()

    fun addIncrement(
        value: Int,
        counter: Counter,
        logHealthConnect: Boolean,
        date: String? = null,
        notes: String? = null
    ) = viewModelScope.launch {
        repository.addIncrement(value, counter.uid, date, notes)

        analytics?.logEvent("add_increment") {
            param("increment_value", value.toLong())
        }
        CountersApplication.prefs!!.tipsStatus = CountersApplication.prefs!!.tipsStatus+1

        if (logHealthConnect) {
            healthConnect.writeActivitySession(
                if (date == null) ZonedDateTime.now()
                else ZonedDateTime.parse(
                    date,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault())
                ),
                counter.displayName,
                counter.healthConnectType,
                value,
                notes
            )
        }
    }

    fun addCounter(counter: Counter) = viewModelScope.launch {
        repository.addCounter(counter)
    }

    fun deleteIncrement(increment: Increment) = viewModelScope.launch {
        repository.deleteIncrement(increment)
    }

    fun deleteCounterById(counterID: Int) = viewModelScope.launch {
        repository.deleteCounterById(counterID)
    }

    fun updateCounter(counter: Counter) = viewModelScope.launch {
        repository.updateCounter(counter)
    }
}

class CountersListViewModelFactory(private val repository: CountersListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountersListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountersListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}