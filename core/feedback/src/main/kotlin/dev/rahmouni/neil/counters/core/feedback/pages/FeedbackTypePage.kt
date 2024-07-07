/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.core.feedback.pages

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.feedback.FeedbackMessages
import dev.rahmouni.neil.counters.core.feedback.FeedbackOptions
import dev.rahmouni.neil.counters.core.feedback.R
import kotlinx.coroutines.delay

@Composable
internal fun FeedbackTypePage(feedbackType: String, nextPage: (String) -> Unit) {
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
                stringResource(R.string.core_feedback_typePage_welcomeMessage),
                stringResource(R.string.core_feedback_typePage_typeMessage),
            ),
        )

        AnimatedVisibility(
            visible = trigger,
            enter = fadeIn(tween(150, 150)) + expandVertically(),
        ) {
            FeedbackOptions(
                mapOf(
                    "BUG" to stringResource(R.string.core_feedback_typePage_bug),
                    "FEATURE" to stringResource(
                        R.string.core_feedback_typePage_suggestion,
                    ),
                ),
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
            Text(stringResource(R.string.core_feedback_continueButton_title))
        }
    }
}
