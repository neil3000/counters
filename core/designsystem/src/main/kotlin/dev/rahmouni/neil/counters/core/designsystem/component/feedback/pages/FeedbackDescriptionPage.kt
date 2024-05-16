package dev.rahmouni.neil.counters.core.designsystem.component.feedback.pages

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.feedback.FeedbackMessages

@Composable
internal fun FeedbackDescriptionPage(
    bug: Boolean,
    feedbackDescription: String,
    nextPage: (String) -> Unit,
    previousPage: (String) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }

    var currentDescription by rememberSaveable { mutableStateOf(feedbackDescription) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    //TODO i18n
    Column {
        FeedbackMessages(
            listOf(
                if (bug) "Please describe the bug and how it happened in detail" else "Explain your suggestion and try to give examples",
                "⚠\uFE0F Do not include personal information",
            ),
        )

        TextField(
            value = currentDescription,
            onValueChange = { currentDescription = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester),
            label = { Text(text = "Description") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    nextPage(currentDescription)
                },
            ),
        )

        Row(
            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp),
            horizontalArrangement = spacedBy(8.dp),
        ) {
            FilledTonalButton(
                onClick = { previousPage(currentDescription) },
            ) {
                Text("Back")
            }
            Button(
                onClick = { nextPage(currentDescription) },
                Modifier.fillMaxWidth(),
                enabled = currentDescription.isNotBlank(),
            ) {
                Text("Continue")
            }
        }
    }
}