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

package dev.rahmouni.neil.counters.core.feedback

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction

sealed interface FeedbackContext {
    data object FeedbackEmptyContext : FeedbackContext
    data class FeedbackScreenContext(
        private val localName: String,
        private val localID: String,
    ) : FeedbackContext {

        fun getID(context: Context): String {
            return "RahNeil_N3:$localID:$localName:" + context.resources.getString(R.string.core_feedback_localeID)
        }

        @Composable
        fun toTopAppBarAction(navigateToFeedback: (Context, FeedbackContext) -> Unit): TopAppBarAction =
            LocalContext.current.let { context ->
                TopAppBarAction(
                    Icons.Outlined.Feedback,
                    stringResource(R.string.core_feedback_topAppBarAction_title),
                ) {
                    navigateToFeedback(context, this)
                }
            }
    }
}
