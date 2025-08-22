package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.R

@Composable
fun TileRemoteConfig() {
    val haptics = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var openDialog by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text("Remote Config") },
        leadingContent = { Icon(Icons.Outlined.Science, null) },
        modifier = Modifier
            .clickable(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                    openDialog = true
                }
            )
    )

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            text = {
                LazyColumn {
                    remoteConfig.all.entries.forEach { (key, value) ->
                        var fVal: Any? = null
                        try {
                            fVal = value.asBoolean()
                        } catch (_: IllegalArgumentException) {
                        }

                        var source = "\uD83D\uDFE5 "

                        when (value.source) {
                            FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> source = "\uD83D\uDFE6 "
                            FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT -> source = "\uD83D\uDFE9 "
                        }

                        when {
                            fVal is Boolean -> {
                                item {
                                    TileSwitch(
                                        title = source + key,
                                        icon = Icons.Outlined.ToggleOn,
                                        checked = value.asBoolean()
                                    ) {}
                                }
                            }

                            value.asString().startsWith("{") -> {
                                item {
                                    TileClick(
                                        title = source + key,
                                        description = value.asString(),
                                        icon = Icons.Outlined.Code,
                                    ) {}
                                }
                            }

                            else -> {
                                item {
                                    TileClick(
                                        title = source + key,
                                        description = value.asString(),
                                        icon = Icons.Outlined.TextFields,
                                    ) {}
                                }
                            }
                        }
                        item { HorizontalDivider() }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.tileRemoteConfig_dialog_confirmButton_text))
                }
            }
        )
    }
}