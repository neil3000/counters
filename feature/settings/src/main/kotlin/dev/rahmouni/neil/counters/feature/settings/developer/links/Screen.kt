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

import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.AnimatedNumber
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.feature.settings.R
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.DeveloperSettingsLinksUiState
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.DeveloperSettingsLinksViewModel
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.DeveloperSettingsLinksData
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.PreviewParameterData

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
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DeveloperSettingsLinksScreen(
    modifier: Modifier = Modifier,
    uiState: DeveloperSettingsLinksUiState,
    onBackIconButtonClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_settings_developerSettingsLinksScreen_topAppBarTitle),
        onBackIconButtonClicked,
    ) {
        DeveloperSettingsLinksPanel(it, uiState.developerSettingsLinksData)
    }
}

@Composable
private fun DeveloperSettingsLinksPanel(
    contentPadding: PaddingValues,
    data: DeveloperSettingsLinksData,
) {
    val context = LocalContext.current

    LazyColumn(contentPadding = contentPadding) {
        items(data.links) { link ->
            Rn3TileClick(
                title = link.path,
                icon = Icons.Outlined.Link,
                supportingText = "${if (link.description.isNotEmpty()) link.description + "\n" else ""}${link.redirectUrl}",
                trailingContent = {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        AnimatedNumber(currentValue = link.clicks) { targetValue ->
                            Box(Modifier.sizeIn(36.dp, 36.dp)) {
                                Text(
                                    text = targetValue.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.align(Alignment.Center),
                                )
                            }
                        }
                    }
                },
            ) {
                Toast.makeText(context, link.redirectUrl, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DeveloperSettingsLinksScreen(uiState = DeveloperSettingsLinksUiState(PreviewParameterData.linksData_default))
    }
}
