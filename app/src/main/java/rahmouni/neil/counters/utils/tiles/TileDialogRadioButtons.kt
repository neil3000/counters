package rahmouni.neil.counters.utils.tiles

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun TileDialogRadioButtons(
    title: String,
    icon: ImageVector,
    values: List<TileDialogRadioListEnum>,
    selected: TileDialogRadioListEnum,
    onChange: (TileDialogRadioListEnum) -> Unit
) {

    val localHapticFeedback = LocalHapticFeedback.current
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogValue by rememberSaveable { mutableStateOf(selected) }

    fun closeDialog() {
        openDialog = false
        dialogValue = selected
    }

    ListItem(
        text = { androidx.compose.material.Text(title) },
        secondaryText = { androidx.compose.material.Text(stringResource(selected.formatted())) },
        singleLineSecondaryText = true,
        icon = { Icon(icon, null) },
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
            onDismissRequest = { closeDialog() },
            title = {
                Text(title)
            },
            icon = { Icon(icon, null) },
            text = {
                LazyColumn {
                    values.forEach {
                        item {
                            val animatedColor = animateColorAsState(
                                if (dialogValue == it) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.background.copy(
                                    alpha = .99f
                                )
                            )
                            val animatedCorners =
                                animateDpAsState(if (dialogValue == it) 24.dp else 16.dp)

                            Surface(
                                color = animatedColor.value,
                                shape = RoundedCornerShape(animatedCorners.value),
                                modifier = Modifier.padding(4.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = dialogValue == it,
                                            onClick = {
                                                localHapticFeedback.performHapticFeedback(
                                                    HapticFeedbackType.LongPress
                                                )

                                                dialogValue = it
                                            },
                                            role = Role.RadioButton
                                        )
                                        .padding(16.dp),
                                ) {
                                    Text(
                                        text = stringResource(it.title()),
                                        color = contentColorFor(animatedColor.value),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                    AnimatedVisibility(dialogValue == it, enter = scaleIn(), exit = scaleOut()) {
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
                TextButton(onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    onChange(dialogValue)

                    openDialog = false
                }) {
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