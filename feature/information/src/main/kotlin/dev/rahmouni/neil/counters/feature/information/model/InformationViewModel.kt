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

package dev.rahmouni.neil.counters.feature.information.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.PhoneInfo
import dev.rahmouni.neil.counters.feature.information.model.InformationUiState.Loading
import dev.rahmouni.neil.counters.feature.information.model.InformationUiState.Success
import dev.rahmouni.neil.counters.feature.information.model.data.InformationData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class InformationViewModel @Inject constructor(
    authHelper: AuthHelper,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    fun save(address: AddressInfo, phone: PhoneInfo) {
        viewModelScope.launch {
            userDataRepository.setNeedInformation(false)
            userDataRepository.setAddressInfo(address)
            userDataRepository.setPhoneInfo(phone)
        }
    }

    val uiState: StateFlow<InformationUiState> =
        combine(
            userDataRepository.userData,
            authHelper.getUserFlow(),
        ) { userData, user ->
            Success(
                InformationData(
                    user = user,
                    address = userData.address,
                    phone = userData.phone,
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
