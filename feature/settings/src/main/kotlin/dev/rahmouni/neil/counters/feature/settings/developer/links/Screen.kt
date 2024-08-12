/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.DeveloperSettingsLinksUiState
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.DeveloperSettingsLinksViewModel
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.DeveloperSettingsLinksData
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.PreviewParameterData
import dev.rahmouni.neil.counters.feature.settings.developer.links.ui.Tile
import dev.rahmouni.neil.counters.feature.settings.developer.links.ui.editLinkModal

@Composable
internal fun DeveloperSettingsLinksRoute(
    modifier: Modifier = Modifier,
    viewModel: DeveloperSettingsLinksViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeveloperSettingsLinksScreen(
        modifier = modifier,
        uiState = uiState,
        onBackIconButtonClicked = navController::popBackStack,
        onFavoriteLink = viewModel::favoriteLink,
        onConfirmButtonClicked = viewModel::setLink,
        onDeleteButtonClicked = viewModel::deleteLink,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DeveloperSettingsLinksScreen(
    modifier: Modifier = Modifier,
    uiState: DeveloperSettingsLinksUiState,
    onBackIconButtonClicked: () -> Unit = {},
    onFavoriteLink: (LinkRn3UrlRawData, Boolean) -> Unit = { _, _ -> },
    onConfirmButtonClicked: (LinkRn3UrlRawData) -> Unit = { _ -> },
    onDeleteButtonClicked: (String) -> Unit = { _ -> },
) {
    val haptic = getHaptic()

    val openLinkEditModal =
        editLinkModal(onConfirm = onConfirmButtonClicked, onDelete = onDeleteButtonClicked)

    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_settings_developerSettingsLinksScreen_topAppBarTitle),
        onBackIconButtonClicked = onBackIconButtonClicked,
        floatingActionButton = {
            FloatingActionButton(
                modifier = it,
                onClick = {
                    haptic.click()

                    openLinkEditModal(null)
                },
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
            }
        },
    ) {
        DeveloperSettingsLinksPanel(
            paddingValues = it,
            data = uiState.developerSettingsLinksData,
            onFavoriteLink = onFavoriteLink,
            onLongPressLink = openLinkEditModal,
        )
    }
}

@Composable
private fun DeveloperSettingsLinksPanel(
    paddingValues: Rn3PaddingValues,
    data: DeveloperSettingsLinksData,
    onFavoriteLink: (LinkRn3UrlRawData, Boolean) -> Unit,
    onLongPressLink: (LinkRn3UrlRawData) -> Unit,
) {
    LazyColumn(contentPadding = paddingValues.toComposePaddingValues()) {
        items(data.links.sortedBy { if (it.favorite) "a" else "z" + it.path }, key = { it.path }) {
            with(it) {
                Tile(
                    Modifier.animateItem(),
                    onFavorite = { favorited -> onFavoriteLink(this, favorited) },
                    onLongPress = { onLongPressLink(this) },
                )
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
