package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R

@Composable
fun TileDialogRadioList(
    title: String,
    icon: ImageVector,
    values: List<TileDialogRadioListEnum>,
    selected: TileDialogRadioListEnum,
    onChange: (TileDialogRadioListEnum) -> Unit
) {

    val haptics = LocalHapticFeedback.current
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogValue by rememberSaveable { mutableStateOf(selected) }

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(stringResource(selected.formatted())) },
        leadingContent = { Icon(icon, null) },
        modifier = Modifier
            .clickable(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                    dialogValue = selected
                    openDialog = true
                }
            )
    )
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Text(title)
            },
            icon = { Icon(icon, null) },
            text = {
                LazyColumn {
                    values.forEach {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .height(56.dp)
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = dialogValue == it,
                                        onClick = {
                                            haptics.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )

                                            dialogValue = it
                                        },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                            ) {
                                RadioButton(
                                    selected = dialogValue == it,
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
                TextButton(onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                    onChange(dialogValue)

                    openDialog = false
                }) {
                    Text(stringResource(R.string.tileDialogRadioList_dialog_confirmButton_text))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.tileDialogRadioList_dialog_dismissButton_text))
                }
            }
        )
    }
}

interface TileDialogRadioListEnum {
    fun title(): Int
    fun formatted(): Int
}