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
