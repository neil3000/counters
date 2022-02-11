package rahmouni.neil.counters.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CountersTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme =
        if (darkTheme) dynamicDarkColorScheme(LocalContext.current) else dynamicLightColorScheme(
            LocalContext.current
        )

    val colors = if (darkTheme) {
        darkColors(primary = dynamicDarkColorScheme(LocalContext.current).primary)
    } else {
        lightColors(primary = dynamicLightColorScheme(LocalContext.current).primary)
    }
    androidx.compose.material.MaterialTheme(colors = colors, content = {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    })
}