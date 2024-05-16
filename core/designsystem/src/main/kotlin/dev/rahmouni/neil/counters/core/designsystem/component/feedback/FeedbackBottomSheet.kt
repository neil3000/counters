package dev.rahmouni.neil.counters.core.designsystem.component.feedback

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.feedback.pages.FeedbackContextPage
import dev.rahmouni.neil.counters.core.designsystem.component.feedback.pages.FeedbackDescriptionPage
import dev.rahmouni.neil.counters.core.designsystem.component.feedback.pages.FeedbackTypePage
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FeedbackBottomSheet(showBottomSheet: Boolean, closeFeedbackModal: (Boolean) -> Unit) {

    val sheetState =
        rememberModalBottomSheetState(confirmValueChange = { false }, skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                closeFeedbackModal(true)
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = {},
        ) {
            Column {
                Surface(tonalElevation = 2.dp) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = spacedBy(16.dp),
                    ) {
                        Icon(Icons.Outlined.Feedback, null)
                        Text("Send Feedback", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.weight(1f))
                        Rn3IconButton(
                            icon = Icons.Outlined.Close,
                            contentDescription = "Close",
                        ) {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                closeFeedbackModal(true)
                            }
                        }
                    }
                }

                Box {
                    SheetContent()
                }
            }
        }
    }
}

@Composable
private fun SheetContent() {
    var page by rememberSaveable { mutableStateOf("TYPE") }

    var type by rememberSaveable { mutableStateOf("BUG") }
    var description by rememberSaveable { mutableStateOf("") }
    var onCurrentPage by rememberSaveable { mutableStateOf(true) }
    var sendScreenshot by rememberSaveable { mutableStateOf(false) }
    var sendAdditionalInfo by rememberSaveable { mutableStateOf(true) }

    AnimatedContent(
        targetState = page,
        transitionSpec = {
            (expandVertically { height -> height } + fadeIn()).togetherWith(shrinkVertically { height -> height } + fadeOut())
                .using(SizeTransform(clip = false))
        },
        label = "animatedPage", //TODO
    ) { screen ->
        when (screen) {
            "TYPE" -> FeedbackTypePage(type) {
                // Reset description if changed type of feedback
                // if not changed we keep the description so it isn't lost
                if (it != type) description = ""

                type = it
                page = "CONTEXT"
            }

            "CONTEXT" -> FeedbackContextPage(
                bug = type == "BUG",
                onCurrentPage = onCurrentPage,
                sendScreenshot = sendScreenshot,
                sendAdditionalInfo = sendAdditionalInfo,
                nextPage = { onCurrentPageValue, sendScreenshotValue, sendAdditionalInfoValue ->
                    onCurrentPage = onCurrentPageValue
                    sendScreenshot = sendScreenshotValue
                    sendAdditionalInfo = sendAdditionalInfoValue
                    page = "DESCRIPTION"
                },
                previousPage = { onCurrentPageValue, sendScreenshotValue, sendAdditionalInfoValue ->
                    onCurrentPage = onCurrentPageValue
                    sendScreenshot = sendScreenshotValue
                    sendAdditionalInfo = sendAdditionalInfoValue
                    page = "TYPE"
                },
            )

            "DESCRIPTION" -> FeedbackDescriptionPage(
                bug = type == "BUG",
                feedbackDescription = description,
                nextPage = {
                    description = it
                    page = "DESCRIPTION"
                    TODO()
                },
                previousPage = {
                    description = it
                    page = "CONTEXT"
                },
            )
        }
    }
}