package dev.rahmouni.neil.counters.feature.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.data.model.FriendEntity
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rebased.Event

@Composable
fun EventCard(event: Event, friends: List<FriendEntity?> = emptyList(), going: Boolean = false) {
    var goingMutable by remember { mutableStateOf(value = going) }

    Card(
        modifier = Modifier.padding(Rn3SurfaceDefaults.paddingValues.copy(bottom = 0.dp)),
        colors = CardDefaults.cardColors(containerColor = if (event.private) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(modifier = Modifier.padding(Rn3TextDefaults.paddingValues)) {
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)
            Row(
                horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                friends.forEach { friend ->
                    if (friend != null) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Friend avatar",
                        )
                    }
                }
                Spacer(modifier = Modifier.width(2.dp))
                if (goingMutable) {
                    Button(
                        content = { Text(text = "I'm coming") },
                        onClick = { goingMutable = false },
                    )
                } else {
                    OutlinedButton(
                        content = { Text(text = "I want to go") },
                        onClick = { goingMutable = true },
                    )
                }
            }
        }
    }
}
