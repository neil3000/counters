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

package dev.rahmouni.neil.counters.core.datastore

import androidx.datastore.core.DataStore
import dev.rahmouni.neil.counters.core.designsystem.rebased.getCountryFromIso
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.core.model.data.PhoneInfo
import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.flow.map
import rahmouni.neil.counters.core.datastore.UserPreferences
import rahmouni.neil.counters.core.datastore.copy
import javax.inject.Inject

class Rn3PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data.map { userPref ->
        UserData(
            hasAccessibilityEmphasizedSwitchesEnabled = userPref.accessibilityHasEmphasizedSwitchesEnabled,
            hasAccessibilityIconTooltipsEnabled = !userPref.accessibilityHasIconTooltipsDisabled,
            hasAccessibilityAltTextEnabled = userPref.accessibilityHasAltTextEnabled,
            hasTravelModeEnabled = userPref.hasTravelModeEnabled,
            hasFriendsMainEnabled = userPref.hasFriendsMainEnabled,
            hasMetricsEnabled = !userPref.hasMetricsDisabled,
            hasCrashlyticsEnabled = !userPref.hasCrashlyticsDisabled,
            shouldShowLoginScreenOnStartup = !userPref.shouldNotShowLoginScreenOnStartup,
            isAppFirstLaunch = !userPref.isNotAppFirstLaunch,
            needInformation = !userPref.isInformationValid,
            address = AddressInfo(
                country = Country.getCountryFromIso(userPref.address.country),
                region = userPref.address.region.ifBlank { null },
                locality = userPref.address.locality,
                street = userPref.address.street,
                postalCode = userPref.address.postalCode.ifBlank { null },
                auxiliaryDetails = userPref.address.auxiliaryDetails.ifBlank { null },
            ),
            phone = PhoneInfo(
                number = userPref.phone.number.ifBlank { null },
                code = Country.getCountryFromIso(userPref.phone.code),
            ),
        )
    }

    suspend fun setAccessibilityEmphasizedSwitchesPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.accessibilityHasEmphasizedSwitchesEnabled = value }
        }
    }

    suspend fun setAccessibilityIconTooltipsPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.accessibilityHasIconTooltipsDisabled = !value }
        }
    }

    suspend fun setAccessibilityAltTextPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.accessibilityHasAltTextEnabled = value }
        }
    }

    suspend fun setMetricsEnabledPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasMetricsDisabled = !value }
        }
    }

    suspend fun setCrashlyticsEnabledPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasCrashlyticsDisabled = !value }
        }
    }

    suspend fun setTravelModeEnabledPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasTravelModeEnabled = value }
        }
    }

    suspend fun setFriendsMainEnabledPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasFriendsMainEnabled = value }
        }
    }

    suspend fun setShouldShowLoginScreenOnStartup(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.shouldNotShowLoginScreenOnStartup = !value }
        }
    }

    suspend fun setNeedInformation(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.isInformationValid = !value }
        }
    }

    suspend fun setNotAppFirstLaunch() {
        userPreferences.updateData {
            it.copy { this.isNotAppFirstLaunch = true }
        }
    }

    suspend fun setAddressInfo(addressInfo: AddressInfo) {
        userPreferences.updateData {
            it.copy {
                this.address = this.address.copy {
                    this.country = addressInfo.country?.isoCode ?: ""
                    this.region = addressInfo.region ?: ""
                    this.locality = addressInfo.locality
                    this.street = addressInfo.street
                    this.postalCode = addressInfo.postalCode ?: ""
                    this.auxiliaryDetails = addressInfo.auxiliaryDetails ?: ""
                }
            }
        }
    }

    suspend fun setPhoneInfo(phoneInfo: PhoneInfo) {
        userPreferences.updateData {
            it.copy {
                this.phone = this.phone.copy {
                    this.code = phoneInfo.code?.isoCode ?: ""
                    this.number = phoneInfo.number ?: ""
                }
            }
        }
    }
}
