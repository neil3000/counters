package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

@Composable
internal fun FeedbackOptions(
    options: Map<String, String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    val haptic = getHaptic()

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        options.forEach { (key, title) ->
            ExtendedFloatingActionButton(
                text = { Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                icon = {
                    AnimatedVisibility(
                        visible = selectedOption == key,
                        enter = fadeIn() + scaleIn() + expandHorizontally(),
                        exit = fadeOut() + scaleOut() + shrinkHorizontally(),
                    ) {
                        Icon(Icons.Outlined.Check, null)
                    }
                },
                onClick = {
                    haptic.click()
                    onOptionSelected(key)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                containerColor = if (key == selectedOption) FloatingActionButtonDefaults.containerColor else MaterialTheme.colorScheme.surfaceVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            )
        }
    }
}