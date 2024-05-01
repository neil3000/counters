package dev.rahmouni.neil.counters.feature.settings.accessibility.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData2

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [AccessibilitySettingsData] for Composable previews.
 */
class AccessibilitySettingsDataPreviewParameterProvider : PreviewParameterProvider<AccessibilitySettingsData> {
    override val values: Sequence<AccessibilitySettingsData> = sequenceOf(accessibilitySettingsData, accessibilitySettingsData2)
}

object PreviewParameterData {
    val accessibilitySettingsData = AccessibilitySettingsData(
        hasEmphasizedSwitchesEnabled = false,
        hasIconTooltipsEnabled = true
    )
    val accessibilitySettingsData2 = AccessibilitySettingsData(
        hasEmphasizedSwitchesEnabled = true,
        hasIconTooltipsEnabled = true
    )
}