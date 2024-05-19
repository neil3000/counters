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

import android.content.Context
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackEmptyContext.getID
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext

const val FEEDBACK_ROUTE = "feedback"

fun NavController.navigateToFeedback(
    context: Context,
    feedbackContext: FeedbackContext,
    navOptions: NavOptions? = null,
    page: String = "type",
    type: String = "BUG",
    description: String = "",
    onCurrentPage: Boolean = true,
    sendScreenshot: Boolean = false,
    sendAdditionalInfo: Boolean = true,
) =
    navigate(
        "$FEEDBACK_ROUTE?${
            if (feedbackContext is FeedbackScreenContext) "contextID=${
                feedbackContext.getID(
                    context,
                )
            }" else ""
        }&page=${page}&type=${type}&description=${description}&onCurrentPage=${onCurrentPage}&sendScreenshot=${sendScreenshot}&sendAdditionalInfo=${sendAdditionalInfo}",
        navOptions,
    )

fun NavGraphBuilder.feedbackDialog(navController: NavController) {
    dialog(
        route = "$FEEDBACK_ROUTE?contextID={contextID}&page={page}&type={type}&description={description}&onCurrentPage={onCurrentPage}&sendScreenshot={sendScreenshot}&sendAdditionalInfo={sendAdditionalInfo}",
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        arguments = listOf(
            navArgument("contextID") {
                defaultValue = null
                nullable = true
            },
            navArgument("page") { defaultValue = "type" },
            navArgument("type") { defaultValue = "BUG" },
            navArgument("description") { defaultValue = "" },
            navArgument("onCurrentPage") { defaultValue = true },
            navArgument("sendScreenshot") { defaultValue = false },
            navArgument("sendAdditionalInfo") { defaultValue = true },
        ),
    ) {
        FeedbackBottomSheet(
            navController,
            it.arguments?.getString("contextID"),
            it.arguments?.getString("page") ?: "type",
            it.arguments?.getString("type") ?: "BUG",
            it.arguments?.getString("description") ?: "",
            it.arguments?.getBoolean("onCurrentPage") ?: true,
            it.arguments?.getBoolean("sendScreenshot") ?: false,
            it.arguments?.getBoolean("sendAdditionalInfo") ?: true,
        )
    }
}