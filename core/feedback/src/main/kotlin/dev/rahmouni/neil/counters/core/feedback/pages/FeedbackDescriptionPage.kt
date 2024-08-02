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

package dev.rahmouni.neil.counters.core.feedback.pages

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
import dev.rahmouni.neil.counters.core.feedback.R.string

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
            messages =
            listOf(
                if (bug) {
                    stringResource(string.core_feedback_descriptionPage_bugMessage)
                } else {
                    stringResource(
                        string.core_feedback_descriptionPage_suggestionMessage,
                    )
                },
                stringResource(string.core_feedback_descriptionPage_personalInfoMessage),
            ),
        )

        TextField(
            value = currentDescription,
            onValueChange = { currentDescription = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester),
            label = { Text(text = stringResource(string.core_feedback_descriptionPage_textField_label)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    nextPage(currentDescription)
                },
            ),
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = spacedBy(8.dp),
        ) {
            FilledTonalButton(
                onClick = {
                    haptic.click()
                    previousPage(currentDescription)
                },
            ) {
                Text(text = stringResource(string.core_feedback_backButton_title))
            }
            Button(
                onClick = {
                    haptic.click()
                    nextPage(currentDescription)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = currentDescription.isNotBlank(),
            ) {
                Text(text = stringResource(string.core_feedback_continueButton_title))
            }
        }
    }
}
