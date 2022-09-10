package rahmouni.neil.counters.utils.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R

@Composable
fun ConfirmationDialog(
    title: String,
    body: @Composable () -> Unit,
    icon: ImageVector,
    confirmLabel: String,
    onConfirm: () -> Unit,
    child: @Composable (() -> Unit) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    var openDialog by rememberSaveable { mutableStateOf(false) }

    child { openDialog = true }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = title)
            },
            icon = { Icon(icon, null) },
            text = body,
            confirmButton = {
                Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = -LocalAbsoluteTonalElevation.current,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.requiredWidthIn(min = 280.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    openDialog = false
                                }
                                .padding(16.dp)
                                .fillMaxWidth(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.confirmationDialog_dismissButton_text),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(4.dp)
                            )
                            Icon(
                                Icons.Outlined.Close,
                                null,
                                Modifier
                                    .padding(start = 16.dp)
                                    .width(24.dp)
                            )
                        }
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.requiredWidthIn(min = 280.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onConfirm()
                                    openDialog = false
                                }
                                .padding(16.dp)
                                .fillMaxWidth(1f)
                        ) {
                            Text(
                                text = confirmLabel,
                                color = MaterialTheme.colorScheme.onError,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(4.dp)
                            )
                            Icon(
                                icon,
                                null,
                                Modifier
                                    .padding(start = 16.dp)
                                    .width(24.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}