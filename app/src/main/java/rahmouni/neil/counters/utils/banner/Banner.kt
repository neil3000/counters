package rahmouni.neil.counters.utils.banner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Banner(
    title: String,
    description: String,
    icon: ImageVector,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    buttons: (@Composable () -> Unit)? = null
) {
    Card(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                Modifier.padding(8.dp),
                Arrangement.spacedBy(24.dp),
                Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    tonalElevation = -LocalAbsoluteTonalElevation.current
                ) {
                    Icon(icon, null, Modifier.padding(12.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(title, style = MaterialTheme.typography.titleLarge)
                    Text(description, style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (buttons != null) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    buttons()
                }
            }
        }
    }
}