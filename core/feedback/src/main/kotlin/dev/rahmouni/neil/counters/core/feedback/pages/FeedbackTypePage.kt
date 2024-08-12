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

package dev.rahmouni.neil.counters.core.feedback.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.feedback.FeedbackMessages
import dev.rahmouni.neil.counters.core.feedback.FeedbackOptions
import dev.rahmouni.neil.counters.core.feedback.R.string
import kotlinx.coroutines.delay

@Composable
internal fun FeedbackTypePage(feedbackType: String, nextPage: (String) -> Unit) {
    val haptic = getHaptic()

    var trigger by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(timeMillis = 1750)
        trigger = true
    }

    var currentType by rememberSaveable { mutableStateOf(feedbackType) }

    Column {
        Spacer(modifier = Modifier.height(8.dp))

        FeedbackMessages(
            messages =
            listOf(
                stringResource(string.core_feedback_typePage_welcomeMessage),
                stringResource(string.core_feedback_typePage_typeMessage),
            ),
        )

        AnimatedVisibility(
            visible = trigger,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 150,
                    delayMillis = 150,
                ),
            ) + expandVertically(),
        ) {
            FeedbackOptions(
                options =
                mapOf(
                    "BUG" to stringResource(string.core_feedback_typePage_bug),
                    "FEATURE" to stringResource(
                        string.core_feedback_typePage_suggestion,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(Rn3SurfaceDefaults.paddingValues.copy(top = 6.dp, bottom = 12.dp)),
            enabled = trigger,
        ) {
            Text(text = stringResource(string.core_feedback_continueButton_title))
        }
    }
}
