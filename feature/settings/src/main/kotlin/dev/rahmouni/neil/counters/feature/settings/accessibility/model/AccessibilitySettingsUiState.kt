package dev.rahmouni.neil.counters.feature.settings.accessibility.model

import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsData

sealed interface AccessibilitySettingsUiState {
    data object Loading : AccessibilitySettingsUiState
    data class Success(val accessibilitySettingsData: AccessibilitySettingsData) :
        AccessibilitySettingsUiState
}