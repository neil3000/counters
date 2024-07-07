package dev.rahmouni.neil.counters.core.data.repository.userData

import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper
import dev.rahmouni.neil.counters.core.data.repository.logAccessibilityEmphasizedSwitchesPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logAccessibilityIconTooltipsPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logCrashlyticsPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logMetricsPreferenceChanged
import dev.rahmouni.neil.counters.core.datastore.Rn3PreferencesDataSource
import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val rn3PreferencesDataSource: Rn3PreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> = rn3PreferencesDataSource.userData

    override suspend fun setAccessibilityEmphasizedSwitches(value: Boolean) {
        rn3PreferencesDataSource.setAccessibilityEmphasizedSwitchesPreference(value)
        analyticsHelper.logAccessibilityEmphasizedSwitchesPreferenceChanged(value)
    }

    override suspend fun setAccessibilityIconTooltips(value: Boolean) {
        rn3PreferencesDataSource.setAccessibilityIconTooltipsPreference(value)
        analyticsHelper.logAccessibilityIconTooltipsPreferenceChanged(value)
    }

    override suspend fun setMetricsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setMetricsEnabledPreference(value)
        analyticsHelper.logMetricsPreferenceChanged(value)
    }

    override suspend fun setCrashlyticsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setCrashlyticsEnabledPreference(value)
        analyticsHelper.logCrashlyticsPreferenceChanged(value)
    }

    override suspend fun setShouldShowLoginScreenOnStartup(value: Boolean) =
        rn3PreferencesDataSource.setShouldShowLoginScreenOnStartup(value)

    override suspend fun setNotAppFirstLaunch() = rn3PreferencesDataSource.setNotAppFirstLaunch()
}