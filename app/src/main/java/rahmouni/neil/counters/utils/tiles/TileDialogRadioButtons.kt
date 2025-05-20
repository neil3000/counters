package rahmouni.neil.counters.utils.tiles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R

@Composable
fun TileDialogRadioButtons(
    title: String,
    dialogTitle: String? = null,
    icon: ImageVector,
    values: List<TileDialogRadioListEnum>,
    selected: TileDialogRadioListEnum?,
    defaultSecondary: String? = null,
    enabled: Boolean = true,
    onChange: (TileDialogRadioListEnum) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogValue by rememberSaveable { mutableStateOf(selected) }

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = {
            Text(if (selected != null) stringResource(selected.formatted()) else defaultSecondary!!)
        },
        leadingContent = { Icon(icon, null) },
        modifier = Modifier
            .alpha(if (enabled) 1f else .7f)
            .clickable(
                enabled = enabled,
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
                Text(dialogTitle ?: title)
            },
            icon = { Icon(icon, null) },
            text = {
                Column(Modifier.width(IntrinsicSize.Max)) {
                    values.forEach {
                        val animatedColor = animateColorAsState(
                            if (dialogValue == it) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            tween(400)
                        )
                        val animatedCorners =
                            animateDpAsState(
                                if (dialogValue == it) 28.dp else 16.dp, tween(400)
                            )

                        Surface(
                            color = animatedColor.value,
                            tonalElevation = -LocalAbsoluteTonalElevation.current,
                            shape = RoundedCornerShape(animatedCorners.value),
                            modifier = Modifier
                                .padding(4.dp)
                                .requiredWidthIn(min = 280.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
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
                                    .padding(16.dp)
                                    .fillMaxWidth(1f)
                            ) {
                                Text(
                                    text = stringResource(it.title()),
                                    color = contentColorFor(animatedColor.value),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(4.dp)
                                )
                                Box(
                                    Modifier
                                        .padding(start = 16.dp)
                                        .width(24.dp)
                                ) {
                                    this@Column.AnimatedVisibility(
                                        dialogValue == it,
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    ) {
                                        Icon(
                                            Icons.Outlined.Check,
                                            null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            },
            confirmButton = {
                TextButton(
                    enabled = dialogValue != null,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                        onChange(dialogValue!!)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.tileDialogRadioButtons_dialog_confirmButton_text))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.tileDialogRadioButtons_dialog_dismissButton_text))
                }
            }
        )
    }
}