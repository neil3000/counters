package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import dev.rahmouni.neil.counters.core.feedback.FeedbackHelper.EmptyFeedbackContext.getID
import dev.rahmouni.neil.counters.core.feedback.FeedbackHelper.FeedbackContext
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FeedbackBottomSheet(showBottomSheet: Boolean, closeFeedbackModal: (Boolean) -> Unit) {

    val feedbackHelper = LocalFeedbackHelper.current

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                closeFeedbackModal(true)
            },
            sheetState = sheetState,
        ) {
            Column {
                Text((feedbackHelper as FeedbackContext).getID)
                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                closeFeedbackModal(false)
                            }
                        }
                    },
                ) {
                    Text("Send fake feedback")
                }
            }
        }
    }
}