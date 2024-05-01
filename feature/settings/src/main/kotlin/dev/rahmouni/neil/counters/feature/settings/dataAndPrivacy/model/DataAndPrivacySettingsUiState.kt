package dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model

import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.DataAndPrivacySettingsData

sealed interface DataAndPrivacySettingsUiState {
    data object Loading : DataAndPrivacySettingsUiState
    data class Success(val dataAndPrivacySettingsData: DataAndPrivacySettingsData) :
        DataAndPrivacySettingsUiState
}