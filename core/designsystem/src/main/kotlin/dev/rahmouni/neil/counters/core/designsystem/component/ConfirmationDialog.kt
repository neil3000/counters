package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Rn3ConfirmationDialog(
    icon: ImageVector,
    body: @Composable () -> Unit,
    confirmLabel: String,
    onConfirm: () -> Unit,
    content: @Composable (openDialog: () -> Unit) -> Unit,
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }


    content {
        openDialog = true
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = stringResource(R.string.core_designsystem_confirmationDialog_title))
            },
            icon = {
                Icon(
                    icon,
                    null,
                )
            },
            text = body,
            confirmButton = {
                Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                    DialogButton(
                        text = stringResource(R.string.core_designsystem_confirmationDialog_cancelButton_text),
                        icon = Icons.Outlined.Close,
                    ) {
                        openDialog = false
                    }
                    DialogButton(
                        confirmLabel,
                        icon,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.error,
                    ) {
                        openDialog = false

                        onConfirm()
                    }
                }
            },
        )
    }
}

@Composable
private fun DialogButton(
    text: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit,
) {
    val haptics = getHaptic()

    Surface(
        color = color,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.requiredWidthIn(min = 280.dp),
        tonalElevation = -LocalAbsoluteTonalElevation.current,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clickable {
                    haptics.click()
                    onClick()
                }
                .padding(16.dp)
                .fillMaxWidth(1f),
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.contentColorFor(color),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(4.dp),
            )
            Icon(
                icon,
                null,
                Modifier
                    .padding(start = 16.dp)
                    .width(24.dp),
            )
        }
    }
}