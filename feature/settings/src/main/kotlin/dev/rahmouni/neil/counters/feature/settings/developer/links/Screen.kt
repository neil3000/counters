/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.rahmouni.neil.counters.feature.settings.developer.links

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlData
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.feature.settings.R
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.DeveloperSettingsLinksUiState
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.DeveloperSettingsLinksViewModel
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.DeveloperSettingsLinksData
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.PreviewParameterData
import dev.rahmouni.neil.counters.feature.settings.developer.links.ui.Tile

@Composable
internal fun DeveloperSettingsLinksRoute(
    modifier: Modifier = Modifier,
    viewModel: DeveloperSettingsLinksViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeveloperSettingsLinksScreen(
        modifier,
        uiState,
        onBackIconButtonClicked = navController::popBackStack,
        onConfirmButtonClicked = viewModel::setLink,
        onDeleteButtonClicked = viewModel::deleteLink,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DeveloperSettingsLinksScreen(
    modifier: Modifier = Modifier,
    uiState: DeveloperSettingsLinksUiState,
    onBackIconButtonClicked: () -> Unit = {},
    onConfirmButtonClicked: (String, String, String) -> Unit = { _, _, _ -> },
    onDeleteButtonClicked: (String) -> Unit = { _ -> },
) {
    val haptics = getHaptic()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var editing by rememberSaveable { mutableStateOf(false) }
    var currentPath by rememberSaveable { mutableStateOf("") }
    var currentRedirectUrl by rememberSaveable { mutableStateOf("") }
    var currentDescription by rememberSaveable { mutableStateOf("") }

    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_settings_developerSettingsLinksScreen_topAppBarTitle),
        onBackIconButtonClicked,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptics.click()

                    editing = false
                    currentPath = ""
                    currentRedirectUrl = ""
                    currentDescription = ""

                    openBottomSheet = true
                },
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        },
    ) {
        DeveloperSettingsLinksPanel(it, uiState.developerSettingsLinksData) { link ->
            with(link) {
                editing = true
                currentPath = path
                currentRedirectUrl = redirectUrl
                currentDescription = description

                openBottomSheet = true
            }
        }

        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                sheetState = bottomSheetState,
            ) {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = spacedBy(8.dp)) {
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

                    Row(horizontalArrangement = spacedBy(8.dp)) {
                        // deleteButton
                        if (editing) {
                            Button(
                                onClick = {
                                    haptics.click()
                                    openBottomSheet = false

                                    onDeleteButtonClicked(currentPath)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                ),
                            ) {
                                Icon(
                                    Icons.Outlined.DeleteForever,
                                    stringResource(R.string.feature_settings_developerSettingsLinksScreen_deleteButton_icon_contentDescription),
                                )
                            }
                        }

                        // confirmButton
                        Button(
                            onClick = {
                                haptics.click()
                                openBottomSheet = false

                                onConfirmButtonClicked(
                                    currentPath,
                                    currentRedirectUrl,
                                    currentDescription,
                                )
                            },
                            Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.feature_settings_developerSettingsLinksScreen_confirmButton_text))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeveloperSettingsLinksPanel(
    contentPadding: PaddingValues,
    data: DeveloperSettingsLinksData,
    onLongPressLink: (LinkRn3UrlData) -> Unit,
) {
    LazyColumn(contentPadding = contentPadding) {
        items(data.links) {
            it.Tile {
                onLongPressLink(it)
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DeveloperSettingsLinksScreen(
            uiState = DeveloperSettingsLinksUiState(PreviewParameterData.linksData_default),
        )
    }
}
