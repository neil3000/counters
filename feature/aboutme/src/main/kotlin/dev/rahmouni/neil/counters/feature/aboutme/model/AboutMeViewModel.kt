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

package dev.rahmouni.neil.counters.feature.aboutme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.feature.aboutme.R
import dev.rahmouni.neil.counters.feature.aboutme.model.data.AboutMeData
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PortfolioState
import dev.rahmouni.neil.counters.feature.aboutme.model.data.SocialLink
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AboutMeViewModel @Inject constructor() : ViewModel() {

    val uiState: StateFlow<AboutMeUiState> =
        flow {
            val config = FirebaseRemoteConfig.getInstance(Firebase.app("RahNeil_N3"))
            config.setDefaultsAsync(R.xml.feature_aboutme_remote_config_defaults)
            config.fetchAndActivate().await()

            emit(
                AboutMeUiState.Success(
                    AboutMeData(
                        bioShort = config.getString("bio_short").replace("\\n", "\n"),
                        portfolio = PortfolioState.getFromConfigString(config.getString("portfolio_url")),
                        socialLinks = SocialLink.getListFromConfigString(config.getString("socialLinks"))
                    ),
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = AboutMeUiState.Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
