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
        "$FEEDBACK_ROUTE?${
            if (feedbackContext is FeedbackScreenContext) {
                "contextID=${
                    feedbackContext.getID(
                        context,
                    )
                }"
            } else {
                ""
            }
        }&page=$page&type=$type&description=$description&onCurrentPage=$onCurrentPage&sendScreenshot=$sendScreenshot&sendAdditionalInfo=$sendAdditionalInfo",
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
