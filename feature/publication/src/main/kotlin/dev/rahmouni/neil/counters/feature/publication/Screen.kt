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

package dev.rahmouni.neil.counters.feature.publication

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Dialog
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3SmallTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.rebased.FeedType
import dev.rahmouni.neil.counters.core.designsystem.rebased.Post
import dev.rahmouni.neil.counters.core.designsystem.rebased.PostType
import dev.rahmouni.neil.counters.core.designsystem.rebased.SharingScope
import dev.rahmouni.neil.counters.core.designsystem.toRn3FormattedString
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.publication.R.string
import dev.rahmouni.neil.counters.feature.publication.data.Analyse
import dev.rahmouni.neil.counters.feature.publication.data.AnalyseType
import java.time.LocalDateTime

@Composable
internal fun PublicationRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToPublic: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToEvents: () -> Unit,
) {

    PublicationScreen(
        modifier,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "PublicationScreen",
            "MC4xwGf6j0RfzJV4O8tDBEn3BObAfFQr",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onSettingsTopAppBarActionClicked = navigateToSettings,
        onPublicPostButtonClicked = navigateToPublic,
        onFriendsPostButtonClicked = navigateToFriends,
        onEventsPostButtonClicked = navigateToEvents,
    )

    TrackScreenViewEvent(screenName = "Publication")
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun PublicationScreen(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit = {},
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onPublicPostButtonClicked: () -> Unit = {},
    onFriendsPostButtonClicked: () -> Unit = {},
    onEventsPostButtonClicked: () -> Unit = {},
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        SheetState(
            initialValue = SheetValue.Expanded,
            density = LocalDensity.current,
            skipPartiallyExpanded = true,
        ),
    )
    val haptic = getHaptic()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var isAnalyzed by rememberSaveable { mutableStateOf(false) }
    var currentDescription by rememberSaveable { mutableStateOf("") }
    var showEmpty by rememberSaveable { mutableStateOf(false) }
    var showTooLarge by rememberSaveable { mutableStateOf(false) }

    var analyse = Analyse(
        AnalyseType.SUCCESS,
        Post(
            id = "test",
            userId = "test",
            feed = FeedType.PUBLIC,
            sharingScope = SharingScope.STREET,
            location = "Street King James",
            timestamp = LocalDateTime.now(),
            content = currentDescription,
            postType = PostType.TEXT,
        ),
    )

    BottomSheetScaffold(
        modifier = modifier,
        topBar = {
            Rn3SmallTopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        text = stringResource(string.feature_publication_topAppBarTitle),
                    )
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                onBackIconButtonClicked = onBackIconButtonClicked,
                actions = listOfNotNull(
                    feedbackTopAppBarAction,
                    TopAppBarAction(
                        Icons.Outlined.Settings,
                        stringResource(string.feature_publication_topAppBarActions_settings),
                        onSettingsTopAppBarActionClicked,
                    ),
                ),
            )
        },
        scaffoldState = scaffoldState,
        sheetDragHandle = null,
        sheetSwipeEnabled = false,
        sheetShape = RoundedCornerShape(0.dp),
        sheetContent = {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    if (!isAnalyzed) {
                    Rn3IconButton(
                        icon = Icons.Outlined.AddPhotoAlternate,
                        contentDescription = stringResource(string.feature_publication_imageButton_description),
                        onClick = {},
                    )
                    Button(
                        onClick = {
                            haptic.click()
                            if (currentDescription.isNotEmpty()) {
                            isAnalyzed = true
                            analyse = Analyse(
                                AnalyseType.SUCCESS,
                                Post(
                                    id = "test",
                                    userId = "test",
                                    feed = FeedType.PUBLIC,
                                    sharingScope = SharingScope.STREET,
                                    location = "Street King James",
                                    timestamp = LocalDateTime.now(),
                                    content = currentDescription,
                                    postType = PostType.TEXT,
                                ),
                            )
                            } else {
                                showEmpty = true
                            }
                        },
                        enabled = !isAnalyzed && !showEmpty,
                    ) {
                        Text(text = stringResource(string.feature_publication_analyseButton))
                    }
                    } else {
                        Row (
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start) {
                            Rn3Dialog(
                                icon = Icons.Outlined.Report,
                                title = stringResource(string.feature_publication_infoDialog_title),
                                body = {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text(text = stringResource(string.feature_publication_infoDialog_textTitle))
                                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Icon(
                                            Icons.Outlined.Report,
                                            null,
                                            Modifier
                                                .padding(top = 2.dp)
                                                .size(SuggestionChipDefaults.IconSize),
                                            tint = MaterialTheme.colorScheme.secondary,
                                        )

                                        Text(text = stringResource(string.feature_publication_infoDialog_textReport).toRn3FormattedString())
                                    }
                                    }
                                },
                                confirmLabel = stringResource(string.feature_publication_infoDialog_button),
                                onConfirm = { /*TODO*/ },
                            ) {
                                Rn3IconButton(
                                    icon = Icons.Outlined.Info,
                                    contentDescription = stringResource(string.feature_publication_informationButton_description),
                                    onClick = it,
                                )
                            }
                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                text = analyse.result.text(analyse.post.feed.text, analyse.post.sharingScope.text),
                                softWrap = true,
                            )
                        }
                        Button(
                            modifier = Modifier.width(IntrinsicSize.Min),
                            onClick = {
                                haptic.click()
                                if (analyse.post.feed == FeedType.PUBLIC) {
                                    onPublicPostButtonClicked()
                                } else if (analyse.post.feed == FeedType.FRIENDS) {
                                    onFriendsPostButtonClicked()
                                } else if (analyse.post.feed == FeedType.EVENTS) {
                                    onEventsPostButtonClicked()
                                }
                            },
                            enabled = analyse.result == AnalyseType.SUCCESS,
                        ) {
                            Text(text = stringResource(string.feature_publication_postButton))
                        }
                    }
                }
                Spacer(Modifier.imePadding())
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            OutlinedTextField(
                value = currentDescription,
                onValueChange = { text ->
                    if (text.length <= 500) {
                        currentDescription = text
                        showTooLarge = text.length >= 500
                        if (text.isNotEmpty()) showEmpty = false
                        isAnalyzed = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                label = { Text(text = stringResource(string.feature_publication_textField_label)) },
                singleLine = false,
                keyboardActions = KeyboardActions(
                    onDone = {

                    },
                ),
                supportingText = {
                    if (showTooLarge) {
                        Text(
                            stringResource(string.feature_publication_textField_characterLimit),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    if (showEmpty) {
                        Text(
                            stringResource(string.feature_publication_textField_isEmpty),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )
        }
    }
}