/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.settings.developer.links.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ConfirmationDialog
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.rn3ErrorButtonColors
import dev.rahmouni.neil.counters.feature.settings.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun editLinkModal(
    onConfirm: (data: LinkRn3UrlRawData) -> Unit,
    onDelete: (path: String) -> Unit,
): (LinkRn3UrlRawData?) -> Unit {
    val scope = rememberCoroutineScope()
    val haptics = getHaptic()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var data: LinkRn3UrlRawData? = null
    var editing by rememberSaveable { mutableStateOf(false) }
    var currentPath by rememberSaveable { mutableStateOf("") }
    var currentRedirectUrl by rememberSaveable { mutableStateOf("") }
    var currentDescription by rememberSaveable { mutableStateOf("") }

    fun hide() {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                openBottomSheet = false
            }
        }
    }

    val enabled by remember { derivedStateOf { currentPath.isNotBlank() && currentRedirectUrl.isNotBlank() } }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // pathTextField
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = currentPath,
                    onValueChange = { currentPath = it },
                    label = { Text(stringResource(R.string.feature_settings_developerSettingsLinksScreen_pathTextField_label)) },
                    singleLine = true,
                    prefix = { Text(stringResource(R.string.feature_settings_developerSettingsLinksScreen_pathTextField_prefix)) },
                    enabled = !editing,
                )

                // redirectUrlTextField
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = currentRedirectUrl,
                    onValueChange = { currentRedirectUrl = it },
                    label = { Text(stringResource(R.string.feature_settings_developerSettingsLinksScreen_redirectUrlTextField_label)) },
                    singleLine = true,
                )

                // descriptionTextField
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = currentDescription,
                    onValueChange = { currentDescription = it },
                    label = { Text(stringResource(R.string.feature_settings_developerSettingsLinksScreen_descriptionTextField_label)) },
                    singleLine = true,
                )

                Spacer(Modifier)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // deleteButton
                    if (editing) {
                        Rn3ConfirmationDialog(
                            icon = Icons.Outlined.DeleteForever,
                            body = { },
                            confirmLabel = stringResource(R.string.feature_settings_developerSettingsLinksScreen_deleteButton_confirmationDialog_confirmLabel),
                            onConfirm = {
                                hide()
                                onDelete(currentPath)
                            },
                        ) {
                            Button(
                                onClick = {
                                    haptics.click()
                                    it()
                                },
                                colors = ButtonDefaults.rn3ErrorButtonColors(),
                            ) {
                                Icon(
                                    Icons.Outlined.DeleteForever,
                                    stringResource(R.string.feature_settings_developerSettingsLinksScreen_deleteButton_icon_contentDescription),
                                )
                            }
                        }
                    }

                    // confirmButton
                    Button(
                        onClick = {
                            if (enabled) {
                                haptics.click()
                                hide()

                                onConfirm(
                                    (data ?: LinkRn3UrlRawData()).copy(
                                        path = currentPath.trim(),
                                        redirectUrl = currentRedirectUrl.trim()
                                            .takeUnless { it.isBlank() },
                                        description = currentDescription.trim()
                                            .takeUnless { it.isBlank() },
                                    ),
                                )
                            }
                        },
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.feature_settings_developerSettingsLinksScreen_confirmButton_text))
                    }
                }
            }
        }
    }

    return {
        data = it
        editing = it != null
        currentPath = it?.path ?: ""
        currentRedirectUrl = it?.redirectUrl ?: ""
        currentDescription = it?.description ?: ""
        openBottomSheet = true
    }
}
