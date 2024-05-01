package dev.rahmouni.neil.counters.core.config

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Global key used to obtain access to the ConfigHelper through a CompositionLocal.
 */
val LocalConfigHelper = staticCompositionLocalOf<ConfigHelper> {
    // Provide a default ConfigHelper which does nothing. This is so that tests and previews
    // do not have to provide one. For real app builds provide a different implementation.
    StubConfigHelper()
}
