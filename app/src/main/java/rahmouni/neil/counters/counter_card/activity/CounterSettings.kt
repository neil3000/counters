package rahmouni.neil.counters.counter_card.activity

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.counterActivity.goal.CounterGoalSettingsActivity
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.health_connect.HealthConnectAvailability
import rahmouni.neil.counters.health_connect.HealthConnectManager
import rahmouni.neil.counters.health_connect.HealthConnectSettingsActivity
import rahmouni.neil.counters.health_connect.observeAsState
import rahmouni.neil.counters.options.ValueOption
import rahmouni.neil.counters.utils.dialogs.ConfirmationDialog
import rahmouni.neil.counters.utils.tiles.*
import rahmouni.neil.counters.value_types.ValueType

@Composable
fun CounterSettings(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel,
    healthConnectManager: HealthConnectManager
) {
    val activity = (LocalContext.current as Activity)
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()

    val permissionsGranted = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(lifecycleState) {
        permissionsGranted.value =
            healthConnectManager.availability.value == HealthConnectAvailability.INSTALLED && healthConnectManager.hasAllPermissions()
    }

    LazyColumn {
        item {
            TileHeader(stringResource(R.string.counterSettings_tile_general_headerTitle))
        }
        item {
            TileTextInput(
                title = stringResource(R.string.counterSettings_tile_name_title),
                dialogTitle = stringResource(R.string.counterSettings_tile_name_dialogTitle),
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
                title = stringResource(R.string.counterSettings_tile_valueType_title),
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
        if (remoteConfig.getBoolean("issue79__goals")) {
            item {
                TileEndSwitch(
                    checked = counter?.goalEnabled ?: false,
                    onChange = {
                        countersListViewModel.updateCounter(
                            counter!!.copy(
                                goalEnabled = it
                            ).toCounter()
                        )
                    }
                ) { defaultModifier ->
                    TileStartActivity(
                        title = stringResource(R.string.counterSettings_tile_goal_title),
                        icon = Icons.Outlined.EmojiEvents,
                        activity = CounterGoalSettingsActivity::class.java,
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
            TileStartActivity(
                title = stringResource(R.string.counterSettings_tile_cardSettings_title),
                description = stringResource(R.string.counterSettings_tile_cardSettings_secondary),
                icon = Icons.Outlined.SmartButton,
                activity = CardSettingsActivity::class.java,
            ) {
                if (counter != null) {
                    it.putExtra("counterID", counter.uid)
                } else it
            }
        }
        item { Divider() }


        item {
            TileHeader(stringResource(R.string.counterSettings_tile_reset_headerTitle))
        }
        item {
            TileDialogRadioButtons(
                title = stringResource(R.string.counterSettings_tile_frequency_title),
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

        // ResetValue
        item {
            ValueOption(
                title = stringResource(R.string.counterSettings_tile_resetValue_title),
                secondaryFormatter = R.string.counterSettings_tile_resetValue_secondaryFormatter,
                icon = Icons.Outlined.Pin,
                dialogTitle = stringResource(R.string.counterSettings_tile_resetValue_dialogTitle),
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
        item { Divider() }


        item {
            TileHeader(stringResource(R.string.counterSettings_tile_other_headerTitle))
        }
        if ((counter?.valueType?.hasHealthConnectIntegration != false) && remoteConfig.getBoolean("issue114__health_connect")) {
            item {
                TileEndSwitch(
                    checked = permissionsGranted.value && (counter?.healthConnectEnabled ?: false),
                    enabled = permissionsGranted.value,
                    onChange = {
                        countersListViewModel.updateCounter(
                            counter!!.copy(
                                healthConnectEnabled = it
                            ).toCounter()
                        )
                    }
                ) { defaultModifier ->
                    TileStartActivity(
                        title = stringResource(R.string.counterSettings_tile_healthConnect_title),
                        icon = Icons.Outlined.Sync,
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
            ConfirmationDialog(
                title = stringResource(R.string.counterSettings_tile_delete_title),
                body = { Text(stringResource(R.string.counterSettings_tile_delete_dialogMessage)) },
                icon = Icons.Outlined.DeleteForever,
                confirmLabel = stringResource(R.string.counterSettings_tile_delete_dialogConfirmButton),
                onConfirm = {
                    val mainAct = Intent(activity, MainActivity::class.java)
                    mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(mainAct)

                    if (counter != null) {
                        countersListViewModel.deleteCounterById(counter.uid)
                    }
                }
            ) {
                TileClick(
                    title = stringResource(R.string.counterSettings_tile_delete_title),
                    icon = Icons.Outlined.DeleteForever
                ) {
                    it()
                }
            }
        }
    }
}