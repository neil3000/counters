import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Post(
    val timestamp: LocalDateTime,
    val title: String,
    val content: String,
    val place: String,
    val placeIcon: ImageVector,
    val buttons: List<String>? = null,
)

fun getTimeElapsed(timestamp: LocalDateTime): String {
    val now = LocalDateTime.now()
    val minutes = ChronoUnit.MINUTES.between(timestamp, now)
    val hours = ChronoUnit.HOURS.between(timestamp, now)
    val days = ChronoUnit.DAYS.between(timestamp, now)

    return when {
        days > 0 -> if (days == 1L) "Yesterday" else "$days days ago"
        hours > 0 -> "$hours hours ago"
        minutes > 0 -> "$minutes minutes ago"
        else -> "Just now"
    }
}

@Composable
fun Publication(post: Post) {
    Surface() {
        Column(modifier = Modifier.padding(16.dp)) {
            ListItem(
                headlineContent = { Text(text = post.title) },
                supportingContent = { Text(text = getTimeElapsed(post.timestamp)) },
                leadingContent = {
                    Icon(imageVector = post.placeIcon, contentDescription = null)
                },
                trailingContent = {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                },
            )
            Text(
                post.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 57.dp),
            )
            if (post.buttons != null) {
                Row(modifier = Modifier.padding(start = 57.dp)) {
                    post.buttons.forEach { buttonText ->
                        Button(
                            onClick = { /* Handle button click */ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp, end = 8.dp),
                        ) {
                            Text(buttonText)
                        }
                    }
                }
            }
        }
    }
}
