package dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.data.repository.UserDataRepository
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Loading
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.DataAndPrivacySettingsData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DataAndPrivacySettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    val uiState: StateFlow<DataAndPrivacySettingsUiState> =
        userDataRepository.userData
            .map { userData ->
                Success(
                    dataAndPrivacySettingsData = DataAndPrivacySettingsData(
                        hasMetricsEnabled = userData.hasMetricsEnabled,
                        hasCrashlyticsEnabled = userData.hasCrashlyticsEnabled,
                    ),
                )
            }.stateIn(
                scope = viewModelScope,
                initialValue = Loading,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            )

    fun setMetricsEnabled(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setMetricsEnabled(value)
        }
    }

    fun setCrashlyticsEnabled(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setCrashlyticsEnabled(value)
        }
    }
}