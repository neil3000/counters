package dev.rahmouni.neil.counters.feature.login.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCorners

@Composable
internal fun AnonymousTile(shape: Rn3RoundedCorners, onClick: () -> Unit) {
    Surface(tonalElevation = 8.dp, shape = shape.toComposeShape()) {
        Rn3TileClick(
            title = "Without an account",
            icon = Icons.Outlined.NoAccounts,
            supportingContent = { Text("You can sign in later anytime") },
            trailingContent = {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null)
            },
            onClick = onClick,
        )
    }
}