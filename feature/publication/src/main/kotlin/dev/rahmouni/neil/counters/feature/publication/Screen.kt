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
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Loop
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dev.rahmouni.neil.counters.core.data.model.EventFeedRawData
import dev.rahmouni.neil.counters.core.data.model.PostRawData
import dev.rahmouni.neil.counters.core.data.model.TriagingRawData
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Dialog
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3OutlinedTextField
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3SmallTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rebased.PostType
import dev.rahmouni.neil.counters.core.designsystem.rebased.SharingScope
import dev.rahmouni.neil.counters.core.designsystem.rebased.text
import dev.rahmouni.neil.counters.core.designsystem.toRn3FormattedString
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.publication.R.string
import dev.rahmouni.neil.counters.feature.publication.model.AnalyseType
import dev.rahmouni.neil.counters.feature.publication.model.FeedType
import dev.rahmouni.neil.counters.feature.publication.model.PublicationUiState.Loading
import dev.rahmouni.neil.counters.feature.publication.model.PublicationUiState.Success
import dev.rahmouni.neil.counters.feature.publication.model.PublicationViewModel
import dev.rahmouni.neil.counters.feature.publication.model.data.PublicationData

@Composable
internal fun PublicationRoute(
    modifier: Modifier = Modifier,
    viewModel: PublicationViewModel = hiltViewModel(),
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToPublic: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToEvents: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        Loading -> {}
        is Success -> PublicationScreen(
            modifier = modifier,
            data = (uiState as Success).publicationData,
            onBackIconButtonClicked = navController::popBackStack,
            feedbackTopAppBarAction = FeedbackScreenContext(
                localName = "PublicationScreen",
                localID = "MC4xwGf6j0RfzJV4O8tDBEn3BObAfFQr",
            ).toTopAppBarAction(navController::navigateToFeedback),
            onSettingsTopAppBarActionClicked = navigateToSettings,
            onAnalyseButtonClicked = viewModel::addTriagingPost,
            onPublicPostButtonClicked = { postData ->
                navigateToPublic()
                viewModel.addPublicPost(postData)
            },
            onFriendsPostButtonClicked = { postData ->
                navigateToFriends()
                viewModel.addFriendPost(postData)
            },
            onEventsPostButtonClicked = { postData ->
                navigateToEvents()
                viewModel.addEventPost(postData)
            },
        )
    }

    TrackScreenViewEvent(screenName = "Publication")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun PublicationScreen(
    modifier: Modifier = Modifier,
    data: PublicationData,
    onBackIconButtonClicked: () -> Unit = {},
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onAnalyseButtonClicked: (TriagingRawData) -> Unit = {},
    onPublicPostButtonClicked: (PostRawData) -> Unit = {},
    onFriendsPostButtonClicked: (PostRawData) -> Unit = {},
    onEventsPostButtonClicked: (EventFeedRawData) -> Unit = {},
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        SheetState(
            initialValue = SheetValue.Expanded,
            density = LocalDensity.current,
            skipPartiallyExpanded = true,
        ),
    )
    val haptic = getHaptic()

    var location by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    var currentDescription by rememberSaveable { mutableStateOf(if (data.posts.isNotEmpty()) data.posts[0].text else "") }

    val phoneUtil = PhoneNumberUtil.getInstance()

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
                        icon = Outlined.Settings,
                        title = stringResource(string.feature_publication_topAppBarActions_settings),
                        onClick = onSettingsTopAppBarActionClicked,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (data.posts.isEmpty()) {
                        Rn3IconButton(
                            icon = Outlined.AddPhotoAlternate,
                            contentDescription = stringResource(string.feature_publication_imageButton_description),
                            onClick = {},
                        )
                        Button(
                            onClick = {
                                haptic.click()
                                onAnalyseButtonClicked(
                                    TriagingRawData(
                                        userId = "test",
                                        text = currentDescription,
                                    ),
                                )
                            },
                            enabled = currentDescription.isNotEmpty() && data.posts.isEmpty(),
                        ) {
                            Text(text = stringResource(string.feature_publication_analyseButton))
                        }
                    } else if (!data.posts[0].analysed) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(text = "Loading")
                            Icon(imageVector = Outlined.Loop, contentDescription = null)
                        }
                    } else {
                        val post = data.posts[0]
                        if (post.type == PostType.CONTACT && !phoneUtil.isValidNumber(
                                data.phone,
                            )
                        ) {
                            post.analyse = AnalyseType.NEEDPHONE
                        }

                        location = locationDisplay(data)

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Rn3Dialog(
                                icon = Outlined.Report,
                                title = stringResource(string.feature_publication_infoDialog_title),
                                body = {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text(text = stringResource(string.feature_publication_infoDialog_textTitle))
                                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                            Icon(
                                                imageVector = Outlined.Report,
                                                contentDescription = null,
                                                modifier = Modifier
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
                                    icon = Outlined.Info,
                                    contentDescription = stringResource(string.feature_publication_informationButton_description),
                                    onClick = it,
                                )
                            }
                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                text = post.analyse!!.text(
                                    post.feed!!.text(),
                                    post.scope!!.text(),
                                ),
                                softWrap = true,
                            )
                        }
                        Button(
                            modifier = Modifier.width(IntrinsicSize.Min),
                            onClick = {
                                haptic.click()
                                when (post.feed!!) {
                                    FeedType.PUBLIC -> {
                                        onPublicPostButtonClicked(
                                            PostRawData(
                                                userId = data.user.getUid(),
                                                sharingScope = post.scope.toString(),
                                                location = location,
                                                timestamp = Timestamp.now(),
                                                content = post.text,
                                                postType = PostType.TEXT.toString(),
                                                categories = listOf("Test 1", "test 2"),
                                            ),
                                        )
                                    }

                                    FeedType.FRIENDS -> {
                                        onFriendsPostButtonClicked(
                                            PostRawData(
                                                userId = data.user.getUid(),
                                                sharingScope = post.scope.toString(),
                                                location = location,
                                                timestamp = Timestamp.now(),
                                                content = post.text,
                                                postType = PostType.TEXT.toString(),
                                                categories = listOf("Test 1", "test 2"),
                                            ),
                                        )
                                    }

                                    FeedType.EVENTS -> {
                                        onEventsPostButtonClicked(
                                            EventFeedRawData(
                                                userId = data.user.getUid(),
                                                sharingScope = post.scope.toString(),
                                                location = location,
                                                description = post.text,
                                                createdAt = Timestamp.now(),
                                            ),
                                        )
                                    }
                                }
                            },
                            enabled = post.analyse == AnalyseType.SUCCESS,
                        ) {
                            Text(text = stringResource(string.feature_publication_postButton))
                        }
                    }
                }
                Spacer(modifier = Modifier.imePadding())
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Rn3OutlinedTextField(
                modifier = Modifier
                    .imePadding()
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(Rn3SurfaceDefaults.paddingValues),
                readOnly = if (data.posts.isNotEmpty()) !data.posts[0].analysed else false,
                value = currentDescription,
                onValueChange = { newText -> currentDescription = newText },
                maxCharacters = 500,
                label = { Text(text = stringResource(string.feature_publication_textField_label)) },
                singleLine = false,
            )
        }
    }
}

@Composable
fun locationDisplay(data: PublicationData): String {
    return when (data.posts[0].scope) {
        SharingScope.GLOBAL -> ""
        SharingScope.COUNTRY -> data.address.country!!.text()
        SharingScope.REGION -> ""
        SharingScope.CITY -> data.address.locality
        SharingScope.DISTRICT -> ""
        SharingScope.NEIGHBORHOOD -> ""
        SharingScope.STREET -> data.address.street
        SharingScope.BUILDING -> data.address.street
        null -> "Not Specified"
    }
}
