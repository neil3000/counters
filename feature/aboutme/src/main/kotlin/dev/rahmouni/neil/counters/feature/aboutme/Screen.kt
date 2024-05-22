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

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.window.core.layout.WindowWidthSizeClass.Companion.COMPACT
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.TwoPaneStrategy
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.initialize
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SystemBarSpacer
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.feature.aboutme.model.AboutMeUiState
import dev.rahmouni.neil.counters.feature.aboutme.model.AboutMeUiState.Loading
import dev.rahmouni.neil.counters.feature.aboutme.model.AboutMeUiState.Success
import dev.rahmouni.neil.counters.feature.aboutme.model.AboutMeViewModel
import dev.rahmouni.neil.counters.feature.aboutme.model.data.AboutMeData
import dev.rahmouni.neil.counters.feature.aboutme.model.data.AboutMeDataPreviewParameterProvider
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PreviewParameterData.aboutMeData_default
import dev.rahmouni.neil.counters.feature.aboutme.ui.Biography
import dev.rahmouni.neil.counters.feature.aboutme.ui.LoadingPfp
import dev.rahmouni.neil.counters.feature.aboutme.ui.MainActions
import dev.rahmouni.neil.counters.feature.aboutme.ui.SocialLinks

@Composable
internal fun AboutMeRoute(
    modifier: Modifier = Modifier,
    viewModel: AboutMeViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (!FirebaseApp.getApps(context).any { it.name == "RahNeil_N3" }) {
        val options = FirebaseOptions.Builder()
            .setProjectId("rahneil-n3")
            .setApplicationId("1:818465066811:android:c519829569270bdba7b11e")
            .setApiKey("AIzaSyCILHyvBfAdZ9bo7njs0hqY5dV2z5gxvnE")
            .build()
        Firebase.initialize(context, options, "RahNeil_N3")
    }

    AboutMeScreen(
        modifier,
        uiState,
        onBackIconButtonClicked = navController::popBackStack,
        onFeedbackIconButtonClicked = {
            navController.navigateToFeedback(
                context,
                FeedbackScreenContext(
                    "AccessibilitySettingsScreen",
                    "jrKt4Xe58KDipPJsm1iPUijn6BMsNc8g",
                ),
            )
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun AboutMeScreen(
    modifier: Modifier = Modifier,
    uiState: AboutMeUiState,
    onBackIconButtonClicked: () -> Unit = {},
    onFeedbackIconButtonClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_aboutme_aboutMeScreen_scaffold_title),
        onBackIconButtonClicked,
        onFeedbackIconButtonClicked,
        null,
        topAppBarStyle = TopAppBarStyle.SMALL,
    ) {

        val aboutMeData = if (uiState is Success) uiState.aboutMeData else null

        when {
            currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass != COMPACT ->
                TwoPanePanel(
                    aboutMeData,
                    it,
                    HorizontalTwoPaneStrategy(.4f),
                )

            currentWindowAdaptiveInfo().windowPosture.isTabletop ->
                TwoPanePanel(
                    aboutMeData,
                    it,
                    VerticalTwoPaneStrategy(.4f),
                )

            else -> ColumnPanel(aboutMeData, it)
        }
    }
}

@Composable
private fun TwoPanePanel(
    aboutMeData: AboutMeData?,
    paddingValues: PaddingValues,
    strategy: TwoPaneStrategy,
) {
    val context = LocalContext.current

    var finishedLoadingAnimation by remember { mutableStateOf(aboutMeData != null) }

    TwoPane(
        first = {
            LoadingPfp(
                Modifier
                    .fillMaxHeight()
                    .padding(bottom = paddingValues.calculateTopPadding()),
                aboutMeData?.pfp,
                finishedLoadingAnimation,
            ) { finishedLoadingAnimation = true }
        },
        second = {
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                AnimatedVisibility(
                    visible = finishedLoadingAnimation,
                    enter = fadeIn() + expandVertically(),
                ) {
                    Column {
                        if (aboutMeData != null) {
                            Biography(aboutMeData.bioShort)
                            MainActions(aboutMeData.portfolioUri)
                            SocialLinks(aboutMeData.socialLinks)
                            Rn3SystemBarSpacer()
                        }
                    }
                }
            }
        },
        strategy = strategy,
        displayFeatures = calculateDisplayFeatures(activity = context as Activity),
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ColumnPanel(aboutMeData: AboutMeData?, paddingValues: PaddingValues) {

    var finishedLoadingAnimation by remember { mutableStateOf(aboutMeData != null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        LoadingPfp(
            pfp = aboutMeData?.pfp,
            finishedLoadingAnimation = finishedLoadingAnimation,
        ) { finishedLoadingAnimation = true }


        AnimatedVisibility(visible = !finishedLoadingAnimation) {
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
        }

        AnimatedVisibility(
            visible = finishedLoadingAnimation,
            enter = fadeIn() + expandVertically(),
        ) {
            Column(Modifier.padding(top = 24.dp)) {
                if (aboutMeData != null) {
                    Biography(aboutMeData.bioShort)
                    MainActions(aboutMeData.portfolioUri)
                    SocialLinks(aboutMeData.socialLinks)
                    Rn3SystemBarSpacer()
                }
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        AboutMeScreen(uiState = Success(aboutMeData_default))
    }
}

@Rn3PreviewUiStates
@Composable
private fun Loading() {
    Rn3Theme {
        AboutMeScreen(uiState = Loading)
    }
}

@Rn3PreviewUiStates
@Composable
private fun UiStates(
    @PreviewParameter(AboutMeDataPreviewParameterProvider::class)
    aboutMeData: AboutMeData,
) {
    Rn3Theme {
        AboutMeScreen(uiState = Success(aboutMeData))
    }
}