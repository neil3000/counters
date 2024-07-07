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

package dev.rahmouni.neil.counters.feature.aboutme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.feature.aboutme.R
import dev.rahmouni.neil.counters.feature.aboutme.model.data.AboutMeData
import dev.rahmouni.neil.counters.feature.aboutme.model.data.SocialLink
import dev.rahmouni.neil.counters.feature.aboutme.model.data.toPfpData
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
                        pfp = config.getString("pfp_img").toPfpData(),
                        bioShort = config.getString("bio_short").replace("\\n", "\n"),
                        portfolioUri = config.getString("portfolio_url").toRn3Uri { },
                        socialLinks = SocialLink.getListFromConfigString(config.getString("socialLinks")),
                    ),
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = AboutMeUiState.Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
