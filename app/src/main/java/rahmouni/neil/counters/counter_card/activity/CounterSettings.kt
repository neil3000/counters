package rahmouni.neil.counters.counter_card.activity

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.firebase.analytics.logEvent
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.counterActivity.goal.CounterGoalSettingsActivity
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.options.ValueOption
import rahmouni.neil.counters.utils.dialogs.ConfirmationDialog
import rahmouni.neil.counters.utils.tiles.*
import rahmouni.neil.counters.value_types.ValueType

@Composable
fun CounterSettings(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel
) {
    val activity = LocalActivity.current
    val context = LocalContext.current

    LazyColumn {
        item {
            TileHeader(stringResource(R.string.counterSettings_tile_general_headerTitle))
        }
        item {
            TileTextInput(
                title = stringResource(R.string.counterSettings_tile_name_title),
                dialogTitle = stringResource(R.string.counterSettings_tile_name_dialogTitle),
                icon = Icons.Outlined.Title,
                value = counter?.getDisplayName(context) ?: "Counter",
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
                values = ValueType.entries,
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
            TileEndSwitch(
                checked = counter?.isGoalSettingEnabled() ?: false,
                onChange = {
                    analytics?.logEvent("toggled_goals", null)

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
        item { HorizontalDivider() }


        item {
            TileHeader(stringResource(R.string.counterSettings_tile_reset_headerTitle))
        }
        item {
            TileDialogRadioButtons(
                title = stringResource(R.string.counterSettings_tile_frequency_title),
                icon = Icons.Outlined.Event,
                values = ResetType.entries,
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
        item { HorizontalDivider() }


        item {
            TileHeader(stringResource(R.string.counterSettings_tile_other_headerTitle))
        }
            item {
                TileEndSwitch(
                    checked = false,
                    enabled = false,
                    onChange = {
                        // Nothing on this version, check previous versions
                    }
                ) { defaultModifier ->
                    TileStartActivity(
                        title = stringResource(R.string.counterSettings_tile_healthConnect_title),
                        icon = Icons.Outlined.Sync,
                        modifier = defaultModifier,
                        activity = null,
                        enabled = false
                    )
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
                    activity?.startActivity(mainAct)

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