package dev.rahmouni.neil.counters.core.designsystem.component.feedback.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.feedback.FeedbackMessages
import dev.rahmouni.neil.counters.core.feedback.FeedbackOptions
import kotlinx.coroutines.delay

@Composable
internal fun FeedbackTypePage(feedbackType: String, nextPage: (String) -> Unit) {
    //TODO i18n

    val haptic = getHaptic()

    var trigger by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(1750)
        trigger = true
    }

    var currentType by rememberSaveable { mutableStateOf(feedbackType) }

    Column {
        FeedbackMessages(
            listOf(
                "Welcome! \uD83D\uDC4B",
                "What kind of feedback would you like to send?",
            ),
        )

        AnimatedVisibility(
            visible = trigger,
            enter = fadeIn(tween(150, 150)) + expandVertically(),
        ) {
            FeedbackOptions(
                mapOf("BUG" to "Bug report", "FEATURE" to "Suggestion"),
                currentType,
            ) { currentType = it }
        }

        Button(
            onClick = {
                haptic.click()
                nextPage(currentType)
            },
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp),
            enabled = trigger,
        ) {
            Text("Continue")
        }
    }
}