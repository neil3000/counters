/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
            //TODO Feedback unexpected error
        }
    }
}
