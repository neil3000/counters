package rahmouni.neil.counters.counter_card.activity

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.ktx.logEvent
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.IncrementValueType
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.options.*

@Composable
fun CounterSettings(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel,
    innerPadding: PaddingValues
) {
    val activity = (LocalContext.current as? Activity)

    LazyColumn(contentPadding = innerPadding) {
        item {
            NameOption(
                counter?.displayName
                    ?: "Counter"
            ) {
                if (counter != null) {
                    countersListViewModel.updateCounter(
                        counter.copy(
                            displayName = it
                        ).toCounter()
                    )
                }
            }
            MenuDefaults.Divider()


            CounterStyleOption(
                counter?.style ?: CounterStyle.DEFAULT
            ) {
                if (counter != null) {
                    countersListViewModel.updateCounter(
                        counter.copy(
                            style = it
                        ).toCounter()
                    )
                }
            }
            MenuDefaults.Divider()

            ButtonBehaviourOption(
                counter?.incrementType
                    ?: IncrementType.ASK_EVERY_TIME
            ) {
                if (counter != null) {
                    countersListViewModel.updateCounter(
                        counter.copy(
                            incrementType = it
                        ).toCounter()
                    )
                }
            }
            MenuDefaults.Divider()

            if (counter?.incrementType == IncrementType.VALUE) {
                MinusEnabledOption(
                    counter.hasMinus,
                ) {
                    countersListViewModel.updateCounter(
                        counter.copy(
                            hasMinus = it
                        ).toCounter()
                    )
                }
                MenuDefaults.Divider()
            }

            IncrementValueOption(
                counter?.incrementType
                    ?: IncrementType.ASK_EVERY_TIME,
                counter?.incrementValueType
                    ?: IncrementValueType.VALUE,
                counter?.incrementValue ?: 1,
                counter?.hasMinus ?: false
            ) { ivt, v ->
                if (counter != null) {
                    countersListViewModel.updateCounter(
                        counter.copy(
                            incrementValueType = ivt,
                            incrementValue = v
                        ).toCounter()
                    )
                }
            }
            MenuDefaults.Divider()

            ResetTypeOption(
                counter?.resetType
                    ?: ResetType.NEVER
            ) {
                if (counter != null) {

                    analytics?.logEvent("updated_counter") {
                        param("From_ResetType", counter.resetType.toString())
                        param("To_ResetType", it.toString())
                    }

                    countersListViewModel.updateCounter(
                        counter.copy(
                            resetType = it
                        ).toCounter()
                    )
                }
            }
            MenuDefaults.Divider()

            DeleteOption {
                activity?.finish()
                if (counter != null) {
                    countersListViewModel.deleteCounterById(counter.uid)
                }
            }
            MenuDefaults.Divider()
        }
    }
}