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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.feedback.FeedbackMessages
import dev.rahmouni.neil.counters.core.feedback.R

@Composable
internal fun FeedbackDescriptionPage(
    bug: Boolean,
    feedbackDescription: String,
    nextPage: (String) -> Unit,
    previousPage: (String) -> Unit,
) {
    val haptic = getHaptic()

    val focusRequester = remember { FocusRequester() }

    var currentDescription by rememberSaveable { mutableStateOf(feedbackDescription) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column {
        FeedbackMessages(
            listOf(
                if (bug) stringResource(R.string.core_feedback_DescriptionPage_bugMessage) else stringResource(
                    R.string.core_feedback_DescriptionPage_suggestionMessage,
                ),
                stringResource(R.string.core_feedback_DescriptionPage_personalInfoMessage),
            ),
        )

        TextField(
            value = currentDescription,
            onValueChange = { currentDescription = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester),
            label = { Text(text = stringResource(R.string.core_feedback_DescriptionPage_textField_label)) },
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
                onClick = {
                    haptic.click()
                    previousPage(currentDescription)
                },
            ) {
                Text(stringResource(R.string.core_feedback_backButton_title))
            }
            Button(
                onClick = {
                    haptic.click()
                    nextPage(currentDescription)
                },
                Modifier.fillMaxWidth(),
                enabled = currentDescription.isNotBlank(),
            ) {
                Text(stringResource(R.string.core_feedback_continueButton_title))
            }
        }
    }
}