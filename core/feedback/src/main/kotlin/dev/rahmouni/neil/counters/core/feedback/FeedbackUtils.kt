package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun getFeedbackID(localName: String, localID: String): String {
    return "RahNeil_N3:$localID:$localName:" + stringResource(R.string.core_feedback_feedbackID)
}

fun getNonComposableFeedbackID(localName: String, localID: String, message: String): String {
    return "RahNeil_N3:$localID:$localName -- $message"
}

fun getNonComposableFeedbackID(localName: String, localID: String): String {
    return "RahNeil_N3:$localID:$localName"
}