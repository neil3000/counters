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
import dev.rahmouni.neil.counters.feature.settings.R
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
        modifier,
        uiState,
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
    val haptics = getHaptic()

    val openLinkEditModal =
        editLinkModal(onConfirm = onConfirmButtonClicked, onDelete = onDeleteButtonClicked)

    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_settings_developerSettingsLinksScreen_topAppBarTitle),
        onBackIconButtonClicked,
        floatingActionButton = {
            FloatingActionButton(
                modifier = it,
                onClick = {
                    haptics.click()

                    openLinkEditModal(null)
                },
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
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
