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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.feedback.FeedbackMessages
import dev.rahmouni.neil.counters.core.feedback.R.string

@Composable
internal fun FeedbackContextPage(
    bug: Boolean,
    hasContext: Boolean,
    onCurrentPage: Boolean,
    sendScreenshot: Boolean,
    sendAdditionalInfo: Boolean,
    nextPage: (Boolean, Boolean, Boolean) -> Unit,
    previousPage: (Boolean, Boolean, Boolean) -> Unit,
) {
    val haptic = getHaptic()

    var onCurrentPageValue by rememberSaveable { mutableStateOf(onCurrentPage) }
    var sendScreenshotValue by rememberSaveable { mutableStateOf(sendScreenshot) }
    var sendAdditionalInfoValue by rememberSaveable { mutableStateOf(sendAdditionalInfo) }

    Column {
        FeedbackMessages(messages = listOf(stringResource(string.core_feedback_contextPage_contextMessage)))

        if (hasContext) {
            Rn3TileSwitch(
                title = if (bug) {
                    stringResource(string.core_feedback_contextPage_bugPageTile_title)
                } else {
                    stringResource(
                        string.core_feedback_contextPage_suggestionPageTile_title,
                    )
                },
                icon = Icons.Outlined.LocationOn,
                checked = onCurrentPageValue,
            ) {
                onCurrentPageValue = it
            }
        }
        if (bug) {
            // TODO replace `enabled = false` by `enabled = onCurrentPageValue` once that #462 is done
            Rn3TileSwitch(
                title = stringResource(string.core_feedback_contextPage_screenshotTile_title),
                icon = Icons.Outlined.Screenshot,
                checked = onCurrentPageValue && sendScreenshotValue,
                enabled = false,
                supportingText = stringResource(string.core_feedback_contextPage_screenshotTile_supportingText),
            ) {
                sendScreenshotValue = it
            }
        }
        Rn3TileSwitch(
            title = stringResource(string.core_feedback_contextPage_additionalInfoTile_title),
            icon = Icons.Outlined.Android,
            checked = sendAdditionalInfoValue,
            supportingText = stringResource(string.core_feedback_contextPage_additionalInfoTile_supportingText),
        ) {
            sendAdditionalInfoValue = it
        }

        Row(
            Modifier.padding(Rn3ExpandableSurfaceDefaults.paddingValues),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilledTonalButton(
                onClick = {
                    haptic.click()
                    previousPage(
                        onCurrentPageValue,
                        onCurrentPageValue && sendScreenshotValue,
                        sendAdditionalInfoValue,
                    )
                },
            ) {
                Text(text = stringResource(string.core_feedback_backButton_title))
            }
            Button(
                onClick = {
                    haptic.click()
                    nextPage(
                        onCurrentPageValue,
                        bug && onCurrentPageValue && sendScreenshotValue,
                        sendAdditionalInfoValue,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(string.core_feedback_continueButton_title))
            }
        }
    }
}
