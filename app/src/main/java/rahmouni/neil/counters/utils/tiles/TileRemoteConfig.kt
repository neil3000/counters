package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileRemoteConfig() {
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var openDialog by remember { mutableStateOf(false) }

    ListItem(
        text = { androidx.compose.material.Text("Remote Config") },
        icon = { Icon(Icons.Outlined.Science, null) },
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
            text = {
                LazyColumn {
                    remoteConfig.all.entries.forEach { (key, value) ->
                        var fVal: Any? = null
                        try {
                            fVal = value.asBoolean()
                        } catch (e: IllegalArgumentException) {

                        }

                        var source = "\uD83D\uDFE5 "

                        when(value.source) {
                            FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> source = "\uD83D\uDFE6 "
                            FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT -> source = "\uD83D\uDFE9 "
                        }

                        when {
                            fVal is Boolean -> {
                                item {
                                    TileSwitch(
                                        title = source+key,
                                        icon = Icons.Outlined.ToggleOn,
                                        checked = value.asBoolean()
                                    ) {}
                                }
                            }
                            value.asString().startsWith("{") -> {
                                item {
                                    TileClick(
                                        title = source+key,
                                        description = value.asString(),
                                        icon = Icons.Outlined.Code,
                                    ) {}
                                }
                            }
                            else -> {
                                item {
                                    TileClick(
                                        title = source+key,
                                        description = value.asString(),
                                        icon = Icons.Outlined.TextFields,
                                    ) {}
                                }
                            }
                        }
                        item { Divider()}
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.tileRemoteConfig_dialog_confirmButton_text))
                }
            }
        )
    }
}