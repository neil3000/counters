package dev.rahmouni.neil.counters.feature.dashboard.newCounter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector

internal enum class CounterCategory(val displayName: String, val icon: ImageVector) {
    COFFEE("Coffee", Icons.Outlined.Coffee),
    PUSHUPS("Pushups", Icons.Outlined.FitnessCenter), //TODO change to /icons "exercise"
    OTHER("Custom", Icons.Outlined.Tune) //TODO maybe change to /icons "instant mix"
}