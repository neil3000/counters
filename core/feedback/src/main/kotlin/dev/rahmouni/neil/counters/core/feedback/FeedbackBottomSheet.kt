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

package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.feedback.R.string
import dev.rahmouni.neil.counters.core.feedback.pages.FeedbackContextPage
import dev.rahmouni.neil.counters.core.feedback.pages.FeedbackDescriptionPage
import dev.rahmouni.neil.counters.core.feedback.pages.FeedbackTypePage
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FeedbackBottomSheet(
    navController: NavController,
    contextID: String?,
    pageDef: String,
    typeDef: String,
    descriptionDef: String,
    onCurrentPageDef: Boolean,
    sendScreenshotDef: Boolean,
    sendAdditionalInfoDef: Boolean,
) {
    val sheetState =
        rememberModalBottomSheetState(confirmValueChange = { false }, skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            navController.popBackStack()
        },
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        dragHandle = {},
    ) {
        Column {
            Surface(tonalElevation = Rn3SurfaceDefaults.tonalElevation) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp, start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        Icon(imageVector = Outlined.Feedback, contentDescription = null)

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = stringResource(string.core_feedback_topBar_title),
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minWidth = 16.dp),
                    )

                    Rn3IconButton(
                        icon = Outlined.Close,
                        contentDescription = stringResource(string.core_feedback_closeButton_contentDescription),
                    ) {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            navController.popBackStack()
                        }
                    }
                }
            }

            Box {
                var page by rememberSaveable { mutableStateOf(pageDef) }

                var type by rememberSaveable { mutableStateOf(typeDef) }
                var description by rememberSaveable { mutableStateOf(descriptionDef) }
                var onCurrentPage by rememberSaveable { mutableStateOf(onCurrentPageDef) }
                var sendScreenshot by rememberSaveable { mutableStateOf(sendScreenshotDef) }
                var sendAdditionalInfo by rememberSaveable { mutableStateOf(sendAdditionalInfoDef) }

                AnimatedContent(
                    targetState = page,
                    transitionSpec = {
                        (expandVertically { height -> height } + fadeIn()).togetherWith(
                            exit = shrinkVertically { height -> height } + fadeOut(),
                        )
                            .using(SizeTransform(clip = false))
                    },
                    label = "animatedPage",
                ) { screen ->
                    when (screen) {
                        "type" -> FeedbackTypePage(type) {
                            // Reset description if changed type of feedback
                            // if not changed we keep the description so it isn't lost
                            if (it != type) description = ""

                            type = it
                            page = "context"
                        }

                        "context" -> FeedbackContextPage(
                            bug = type == "BUG",
                            hasContext = contextID != null,
                            onCurrentPage = onCurrentPage,
                            sendScreenshot = sendScreenshot,
                            sendAdditionalInfo = sendAdditionalInfo,
                            nextPage = { onCurrentPageValue, sendScreenshotValue, sendAdditionalInfoValue ->
                                onCurrentPage = onCurrentPageValue
                                sendScreenshot = sendScreenshotValue
                                sendAdditionalInfo = sendAdditionalInfoValue

                                page = "description"
                            },
                            previousPage = { onCurrentPageValue, sendScreenshotValue, sendAdditionalInfoValue ->
                                onCurrentPage = onCurrentPageValue
                                sendScreenshot = sendScreenshotValue
                                sendAdditionalInfo = sendAdditionalInfoValue

                                page = "type"
                            },
                        )

                        "description" -> FeedbackDescriptionPage(
                            bug = type == "BUG",
                            feedbackDescription = description,
                            nextPage = {
                                description = it

                                TODO()
                            },
                            previousPage = {
                                description = it

                                page = "context"
                            },
                        )
                    }
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = "FeedbackBottomSheet")
}
