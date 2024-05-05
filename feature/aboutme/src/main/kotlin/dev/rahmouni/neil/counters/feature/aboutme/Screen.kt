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

package dev.rahmouni.neil.counters.feature.aboutme

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.LazyColumnFullScreen
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle
import dev.rahmouni.neil.counters.core.feedback.getFeedbackID
import dev.rahmouni.neil.counters.feature.aboutme.ui.Biography
import dev.rahmouni.neil.counters.feature.aboutme.ui.LoadingPfp
import dev.rahmouni.neil.counters.feature.aboutme.ui.MainActions
import dev.rahmouni.neil.counters.feature.aboutme.ui.SocialLinks
import kotlinx.coroutines.delay

@Composable
internal fun AboutMeRoute(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit,
) {
    AboutMeScreen(
        modifier,
        onBackIconButtonClicked,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun AboutMeScreen(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_aboutme_aboutMeScreen_scaffold_title),
        onBackIconButtonClicked,
        getFeedbackID(
            localName = "AboutMeScreen",
            localID = "NJFWHop0rQkfFOUcSJlcepwG0f7XN8dd",
        ),
        topAppBarStyle = TopAppBarStyle.SMALL,
    ) {
        AboutMePanel(it)
    }
}

@Composable
private fun AboutMePanel(paddingValues: PaddingValues) {
    var finishedLoading by remember { mutableStateOf(false) }
    var finishedLoadingAnimation by remember { mutableStateOf(false) }

    // LOADING LOGIC
    // delay for now
    LaunchedEffect(Unit) {
        delay(400)
        finishedLoading = true
    }

    LazyColumnFullScreen(
        contentPadding = PaddingValues(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            AnimatedVisibility(visible = finishedLoadingAnimation) {
                Column {
                    Spacer(Modifier.padding(paddingValues))

                    Text(
                        text = "Neïl Rahmouni",
                        Modifier.padding(vertical = 24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        fontSize = TextUnit(5f, TextUnitType.Em),
                    )
                }
            }
        }

        item {
            LoadingPfp({ finishedLoading }, finishedLoadingAnimation) {
                finishedLoadingAnimation = true
            }
        }

        item {
            AnimatedVisibility(visible = finishedLoadingAnimation) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Biography()
                    MainActions()
                    SocialLinks()
                }
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        AboutMeScreen()
    }
}
