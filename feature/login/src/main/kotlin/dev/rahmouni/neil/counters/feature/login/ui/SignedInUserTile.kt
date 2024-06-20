package dev.rahmouni.neil.counters.feature.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.user.UserAvatarAndName
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCorners
import dev.rahmouni.neil.counters.core.user.Rn3User

@Composable
internal fun Rn3User.Tile(shape: Rn3RoundedCorners, onClick: () -> Unit) {
    val haptics = getHaptic()

    Surface(tonalElevation = 8.dp, shape = shape.toComposeShape()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        haptics.click()
                        onClick()
                    },
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserAvatarAndName(showEmail = true)
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                null,
            )
        }
    }
}