/*
 * Copyright 2024 Rahmouni Neïl
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

package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface FeedbackHelper {
    data object EmptyFeedbackContext : FeedbackHelper
    data class FeedbackContext(internal val localName: String, internal val localID: String) :
        FeedbackHelper

    val FeedbackContext.getID: String
        @Composable
        get() = "RahNeil_N3:$localID:$localName:" + stringResource(R.string.core_feedback_feedbackID)
}