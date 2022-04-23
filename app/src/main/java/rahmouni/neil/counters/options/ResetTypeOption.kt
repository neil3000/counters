package rahmouni.neil.counters.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun ResetTypeOption(
    resetType: ResetType,
    inModal: Boolean = false,
    onSave: (ResetType) -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogResetType by rememberSaveable { mutableStateOf(ResetType.NEVER) }
    val localHapticFeedback = LocalHapticFeedback.current

    fun closeDialog() {
        openDialog = false
    }

    fun confirm() {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

        onSave(dialogResetType)

        closeDialog()
    }

    ListItem(
        text = { Text(stringResource(R.string.text_reset)) },
        secondaryText = {
            androidx.compose.material.Text(stringResource(resetType.formatted(), 0))
        },
        icon = if (!inModal) {
            { Icon(Icons.Outlined.Event, null) }
        } else null,
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    dialogResetType = resetType
                    openDialog = true
                }
            )
            .padding(if (inModal) 8.dp else 0.dp)
    )
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                closeDialog()
            },
            title = {
                Text(stringResource(R.string.action_reset))
            },
            icon = { Icon(Icons.Outlined.Event, null) },
            text = {
                Column {
                    ResetType.values().forEach {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = dialogResetType == it,
                                    onClick = {
                                        localHapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )

                                        dialogResetType = it
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),

                            ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(56.dp)
                            ) {
                                RadioButton(
                                    selected = dialogResetType == it,
                                    onClick = null
                                )
                                Text(
                                    text = stringResource(it.title()),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { confirm() }) {
                    Text(stringResource(R.string.action_save_short))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        closeDialog()
                    }
                ) {
                    Text(stringResource(R.string.action_cancel_short))
                }
            }
        )
    }
}