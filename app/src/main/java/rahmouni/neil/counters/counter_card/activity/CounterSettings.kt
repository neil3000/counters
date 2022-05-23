package rahmouni.neil.counters.counter_card.activity

import android.app.Activity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.*
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.counter_card.activity.health_connect.HealthConnectSettingsActivity
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.options.IncrementValueOption
import rahmouni.neil.counters.utils.tiles.*

@Composable
fun CounterSettings(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel
) {
    val activity = (LocalContext.current as Activity)
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    LazyColumn {
        item {
            TileHeader(stringResource(R.string.header_general))
        }
        item {
            TileTextInput(
                title = stringResource(R.string.text_name),
                dialogTitle = stringResource(R.string.action_editName),
                icon = Icons.Outlined.Title,
                value = counter?.displayName ?: "Counter",
                validateInput = { it.isNotEmpty() }
            ) {
                if (counter != null) {
                    countersListViewModel.updateCounter(
                        counter.copy(
                            displayName = it
                        ).toCounter()
                    )
                }
            }
        }
        item {
            TileStartActivity(
                title = stringResource(R.string.text_homeScreenSettings),
                icon = Icons.Outlined.GridView,
                activity = CardSettingsActivity::class.java,
            ) {
                if (counter != null) {
                    it.putExtra("counterID", counter.uid)
                } else it
            }
        }
        item { MenuDefaults.Divider() }


        item {
            TileHeader(stringResource(R.string.header_reset))
        }
        item {
            TileDialogRadioButtons(
                title = stringResource(R.string.text_frequency),
                icon = Icons.Outlined.Event,
                values = ResetType.values().toList(),
                selected = counter?.resetType ?: ResetType.NEVER
            ) {
                if (counter != null) {
                    analytics?.logEvent("updated_counter") {
                        param("From_ResetType", counter.resetType.toString())
                        param("To_ResetType", it.toString())
                    }

                    countersListViewModel.updateCounter(
                        counter.copy(
                            resetType = it as ResetType
                        ).toCounter()
                    )
                }
            }
        }
        if (remoteConfig.getBoolean("issue54__reset_value_setting")) {
            item {
                TileNumberInput(
                    title = stringResource(R.string.text_resetValue),
                    dialogTitle = stringResource(R.string.action_resetTo),
                    icon = Icons.Outlined.Pin,
                    value = counter?.resetValue ?: 0,
                    format = R.string.text_resetsToX,
                    enabled = counter?.resetType != ResetType.NEVER
                ) {
                    if (counter != null) {
                        countersListViewModel.updateCounter(
                            counter.copy(
                                resetValue = it
                            ).toCounter()
                        )
                    }
                }
            }
        }
        item { MenuDefaults.Divider() }


        item {
            TileHeader(stringResource(R.string.header_other))
        }
        if (remoteConfig.getBoolean("issue114__gfit_integration")) {
            item {
                TileSwitchStartActivity(
                    title = stringResource(R.string.text_healthConnectIntegration),
                    icon = Icons.Outlined.FitnessCenter,
                    checked = (counter?.healthConnectEnabled ?: false)
                            && healthConnect.isAvailable(),
                    switchEnabled = healthConnect.isAvailable(),
                    activity = HealthConnectSettingsActivity::class.java,
                    extras = {
                        if (counter != null) {
                            it.putExtra("counterID", counter.uid)
                        } else it
                    }
                ) {
                    countersListViewModel.updateCounter(
                        counter!!.copy(
                            healthConnectEnabled = it
                        ).toCounter()
                    )
                }
            }
        }
        item {
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
        }
        item {
            TileConfirmation(
                title = stringResource(R.string.action_deleteCounter),
                icon = Icons.Outlined.DeleteForever,
                message = stringResource(R.string.confirmation_deleteCounter),
                confirmString = stringResource(R.string.action_delete_short)
            ) {
                activity.finish()
                if (counter != null) {
                    countersListViewModel.deleteCounterById(counter.uid)
                }
            }
        }
        item { MenuDefaults.Divider() }
    }
}