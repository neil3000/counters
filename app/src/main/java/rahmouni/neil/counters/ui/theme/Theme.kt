package rahmouni.neil.counters.ui.theme

import android.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CountersTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    fun color(hex: String): androidx.compose.ui.graphics.Color {
        return androidx.compose.ui.graphics.Color(Color.parseColor(hex))
    }

    val colorScheme =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (darkTheme) dynamicDarkColorScheme(LocalContext.current) else dynamicLightColorScheme(
                LocalContext.current
            )
        } else {
            if (darkTheme) darkColorScheme(
                primary = color("#acc7ff"),
                onPrimary = color("#032f67"),
                primaryContainer = color("#24457f"),
                onPrimaryContainer = color("#d6e2ff"),
                inversePrimary = color("#3e5e98"),
                secondary = color("#bfc6dc"),
                onSecondary = color("#283041"),
                secondaryContainer = color("#3f4758"),
                onSecondaryContainer = color("#dbe2f9"),
                tertiary = color("#e5b7e9"),
                onTertiary = color("#44234c"),
                tertiaryContainer = color("#5d3a63"),
                onTertiaryContainer = color("#ffd6ff"),
                outline = color("#8e919a"),
                background = color("#1b1b1d"),
                onBackground = color("#e3e1e6"),
                surface = color("#1b1b1d"),
                onSurface = color("#e3e1e6"),
                surfaceVariant = color("#44464e"),
                onSurfaceVariant = color("#c4c6d0"),
                inverseSurface = color("#e3e1e6"),
                inverseOnSurface = color("#303033"),
            ) else lightColorScheme(
                primary = color("#3e5e98"),
                onPrimary = color("#ffffff"),
                primaryContainer = color("#d6e2ff"),
                onPrimaryContainer = color("#001a43"),
                inversePrimary = color("#acc7ff"),
                secondary = color("#575e71"),
                onSecondary = color("#ffffff"),
                secondaryContainer = color("#dbe2f9"),
                onSecondaryContainer = color("#131c2c"),
                tertiary = color("#77517d"),
                onTertiary = color("#ffffff"),
                tertiaryContainer = color("#ffd6ff"),
                onTertiaryContainer = color("#2d0d35"),
                outline = color("#73767e"),
                background = color("#fdfbff"),
                onBackground = color("#1b1b1d"),
                surface = color("#fdfbff"),
                onSurface = color("#1b1b1d"),
                surfaceVariant = color("#e0e2ec"),
                onSurfaceVariant = color("#44464e"),
                inverseSurface = color("#303033"),
                inverseOnSurface = color("#f2f0f4")
            )
        }

    val colors = if (darkTheme)
        darkColors(
            primary = colorScheme.primary,
            secondary = colorScheme.secondary,
            background = colorScheme.background,
            surface = colorScheme.surface,
            error = colorScheme.error,
        )
    else
        lightColors(
            primary = colorScheme.primary,
            secondary = colorScheme.secondary,
            background = colorScheme.background,
            surface = colorScheme.surface,
            error = colorScheme.error,
        )


    androidx.compose.material.MaterialTheme(colors = colors, content = {
        MaterialTheme(
            content = content,
            colorScheme = colorScheme
        )
    })
}