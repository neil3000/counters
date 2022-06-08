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
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.counter_card.activity.health_connect.HealthConnectSettingsActivity
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.healthConnect
import rahmouni.neil.counters.options.ResetValueOption
import rahmouni.neil.counters.utils.tiles.*
import rahmouni.neil.counters.value_types.ValueType

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
            TileDialogRadioButtons(
                title = stringResource(R.string.text_valueType),
                icon = Icons.Outlined.Category,
                values = ValueType.values().toList(),
                selected = counter?.valueType ?: ValueType.NUMBER
            ) {
                if (counter != null) {
                    countersListViewModel.updateCounter(
                        counter.copy(
                            valueType = it as ValueType
                        ).toCounter()
                    )
                }
            }
        }
        item {
            TileStartActivity(
                title = stringResource(R.string.tile_cardSettings_title),
                description = stringResource(R.string.tile_cardSettings_secondary),
                icon = Icons.Outlined.SmartButton,
                activity = CardSettingsActivity::class.java,
            ) {
                if (counter != null) {
                    it.putExtra("counterID", counter.uid)
                } else it
            }
        }
        if (remoteConfig.getBoolean("issue79__goals")) {
            item {
                TileEndSwitch(checked = false, onChange = {}) {

                }
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
        item {
            ResetValueOption(
                valueType = counter?.valueType ?: ValueType.NUMBER,
                value = counter?.resetValue ?: 0,
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
        item { MenuDefaults.Divider() }


        item {
            TileHeader(stringResource(R.string.header_other))
        }
        if ((counter?.valueType?.hasHealthConnectIntegration != false) && remoteConfig.getBoolean("issue114__gfit_integration")) {
            item {
                TileEndSwitch(
                    checked = (counter?.healthConnectEnabled ?: false)
                            && healthConnect.isAvailable(),
                    enabled = healthConnect.isAvailable(),
                    onChange = {
                        countersListViewModel.updateCounter(
                            counter!!.copy(
                                healthConnectEnabled = it
                            ).toCounter()
                        )
                    }
                ) { defaultModifier ->
                    TileStartActivity(
                        title = stringResource(R.string.text_healthConnectIntegration),
                        icon = Icons.Outlined.FitnessCenter,
                        activity = HealthConnectSettingsActivity::class.java,
                        modifier = defaultModifier
                    ) { intent ->
                        if (counter != null) {
                            intent.putExtra("counterID", counter.uid)
                        } else intent
                    }
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
    }
}