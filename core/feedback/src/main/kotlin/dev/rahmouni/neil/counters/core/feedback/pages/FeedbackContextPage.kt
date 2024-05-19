package dev.rahmouni.neil.counters.core.designsystem.component.feedback.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.feedback.FeedbackMessages

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
    //TODO i18n

    var onCurrentPageValue by rememberSaveable { mutableStateOf(onCurrentPage) }
    var sendScreenshotValue by rememberSaveable { mutableStateOf(sendScreenshot) }
    var sendAdditionalInfoValue by rememberSaveable { mutableStateOf(sendAdditionalInfo) }

    Column {
        FeedbackMessages(listOf("We just need a bit more context:"))

        if (hasContext) {
            Rn3TileSwitch(
                title = if (bug) "Bug was on the current page" else "Suggestion is about this page",
                icon = Icons.Outlined.LocationOn,
                checked = onCurrentPageValue,
            ) {
                onCurrentPageValue = it
            }
        }
        if (bug) {
            Rn3TileSwitch(
                title = "Include screenshot",
                icon = Icons.Outlined.Screenshot,
                checked = onCurrentPageValue && sendScreenshotValue,
                enabled = onCurrentPageValue,
                supportingText = "Only if the issue is a visual one",
            ) {
                sendScreenshotValue = it
            }
        }
        Rn3TileSwitch(
            title = "Send additional info",
            icon = Icons.Outlined.Android,
            checked = sendAdditionalInfoValue,
            supportingText = "Such as the App & Android version",
        ) {
            sendAdditionalInfoValue = it
        }

        Row(
            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilledTonalButton(
                onClick = {
                    previousPage(
                        onCurrentPageValue,
                        onCurrentPageValue && sendScreenshotValue,
                        sendAdditionalInfoValue,
                    )
                },
            ) {
                Text("Back")
            }
            Button(
                onClick = {
                    nextPage(
                        onCurrentPageValue,
                        bug && onCurrentPageValue && sendScreenshotValue,
                        sendAdditionalInfoValue,
                    )
                },
                Modifier.fillMaxWidth(),
            ) {
                Text("Continue")
            }
        }
    }
}