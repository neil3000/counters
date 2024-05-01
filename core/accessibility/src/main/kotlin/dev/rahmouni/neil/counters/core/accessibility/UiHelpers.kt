package dev.rahmouni.neil.counters.core.accessibility

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Global key used to obtain access to the AccessibilityHelper through a CompositionLocal.
 */
val LocalAccessibilityHelper = staticCompositionLocalOf {
    AccessibilityHelper()
}
