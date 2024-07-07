/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.settings.developer.main.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.feature.settings.developer.main.model.data.DeveloperSettingsData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DeveloperSettingsViewModel @Inject constructor(
    authHelper: AuthHelper,
) : ViewModel() {

    val uiState: StateFlow<DeveloperSettingsUiState> =
        authHelper.getUserFlow().map { user ->
            DeveloperSettingsUiState(
                developerSettingsData = DeveloperSettingsData(
                    isUserAdmin = user.isAdmin(),
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = DeveloperSettingsUiState(
                DeveloperSettingsData(
                    isUserAdmin = false,
                ),
            ),
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    internal fun clearPersistence() = viewModelScope.launch {
        try {
            with(Firebase.firestore) {
                terminate().await()
                clearPersistence().await()
            }
        } catch (e: Exception) {
            // TODO Feedback unexpected error
        }
    }
}
