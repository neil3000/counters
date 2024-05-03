package dev.rahmouni.neil.counters.feature.aboutme.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyRowWithPadding

@Composable
fun SocialLinks() {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Text(
                "Socials",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = TextUnit(3.25f, TextUnitType.Em),
            )
            Rn3LazyRowWithPadding(
                Modifier.padding(vertical = 4.dp),
                horizontalPadding = 8.dp,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(8) {
                    item {
                        Rn3IconButton(
                            icon = Icons.Outlined.Link,
                            contentDescription = "",
                        ) {}
                    }
                }
            }
        }
    }
}