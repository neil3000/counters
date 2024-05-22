package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton

data class TopAppBarAction(val icon: ImageVector, val title: String, val onClick: () -> Unit) {

    @Composable
    fun IconButton() {
        Rn3IconButton(icon = this.icon, contentDescription = this.title, onClick = this.onClick)
    }

    @Composable
    fun DropdownMenuItem() {
        DropdownMenuItem(
            text = { Text(this.title) },
            onClick = this.onClick,
            leadingIcon = {
                Icon(this.icon, null)
            },
        )
    }
}

@Composable
fun List<TopAppBarAction>.DropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit) {
    androidx.compose.material3.DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) scope@ {
        this@DropdownMenu.map { it.DropdownMenuItem() }
    }
}