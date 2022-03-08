package rahmouni.neil.counters.options

import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun DeleteOption(
    onDelete: () -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = { androidx.compose.material.Text(stringResource(R.string.action_deleteCounter)) },
        icon = { Icon(Icons.Outlined.Delete, null) },
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    openDialog = true
                }
            )
    )
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = stringResource(R.string.action_deleteCounter_short))
            },
            icon = { Icon(Icons.Outlined.DeleteForever, null) },
            text = {
                Text(stringResource(R.string.confirmation_deleteCounter))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        onDelete()

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.action_delete_short))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.action_cancel_short))
                }
            }
        )
    }
}