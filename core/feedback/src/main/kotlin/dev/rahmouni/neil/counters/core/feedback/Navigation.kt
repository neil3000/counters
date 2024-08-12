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

package dev.rahmouni.neil.counters.core.feedback

import android.content.Context
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
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
        route = "$FEEDBACK_ROUTE?${
            if (feedbackContext is FeedbackScreenContext) {
                "contextID=${
                    feedbackContext.getID(
                        context = context,
                    )
                }"
            } else {
                ""
            }
        }&page=$page&type=$type&description=$description&onCurrentPage=$onCurrentPage&sendScreenshot=$sendScreenshot&sendAdditionalInfo=$sendAdditionalInfo",
        navOptions = navOptions,
    )

fun NavGraphBuilder.feedbackDialog(navController: NavController) {
    dialog(
        route = "$FEEDBACK_ROUTE?contextID={contextID}&page={page}&type={type}&description={description}&onCurrentPage={onCurrentPage}&sendScreenshot={sendScreenshot}&sendAdditionalInfo={sendAdditionalInfo}",
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        arguments = listOf(
            navArgument(name = "contextID") {
                defaultValue = null
                nullable = true
            },
            navArgument(name = "page") { defaultValue = "type" },
            navArgument(name = "type") { defaultValue = "BUG" },
            navArgument(name = "description") { defaultValue = "" },
            navArgument(name = "onCurrentPage") { defaultValue = true },
            navArgument(name = "sendScreenshot") { defaultValue = false },
            navArgument(name = "sendAdditionalInfo") { defaultValue = true },
        ),
    ) {
        FeedbackBottomSheet(
            navController = navController,
            contextID = it.arguments?.getString("contextID"),
            pageDef = it.arguments?.getString("page") ?: "type",
            typeDef = it.arguments?.getString("type") ?: "BUG",
            descriptionDef = it.arguments?.getString("description") ?: "",
            onCurrentPageDef = it.arguments?.getBoolean("onCurrentPage") ?: true,
            sendScreenshotDef = it.arguments?.getBoolean("sendScreenshot") ?: false,
            sendAdditionalInfoDef = it.arguments?.getBoolean("sendAdditionalInfo") ?: true,
        )
    }
}
