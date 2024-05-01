package dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.p1
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.p2
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.p3

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [DataAndPrivacySettingsData] for Composable previews.
 */
class DataAndPrivacySettingsDataPreviewParameterProvider :
    PreviewParameterProvider<DataAndPrivacySettingsData> {
    override val values: Sequence<DataAndPrivacySettingsData> = sequenceOf(p1, p2, p3)
}

object PreviewParameterData {
    val p1 = DataAndPrivacySettingsData(
        hasMetricsEnabled = false,
        hasCrashlyticsEnabled = false,
    )
    val p2 = DataAndPrivacySettingsData(
        hasMetricsEnabled = false,
        hasCrashlyticsEnabled = true,
    )
    val p3 = DataAndPrivacySettingsData(
        hasMetricsEnabled = true,
        hasCrashlyticsEnabled = false,
    )
}