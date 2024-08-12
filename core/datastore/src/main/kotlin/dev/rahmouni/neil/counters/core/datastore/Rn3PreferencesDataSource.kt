/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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

package dev.rahmouni.neil.counters.core.datastore

import androidx.datastore.core.DataStore
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.Country
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
            phone = if (!userPref.phone.number.isNullOrBlank() && !userPref.phone.code.isNullOrBlank()) {
                PhoneNumber().apply {
                    countryCode = Country.getCountryFromIso(userPref.phone.code)!!.phoneCode
                    nationalNumber = userPref.phone.number.toLong()
                }
            } else PhoneNumber(),
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

    suspend fun setPhoneNumber(phoneNumber: PhoneNumber) {
        userPreferences.updateData {
            it.copy {
                this.phone = this.phone.copy {
                    this.code =
                        Country.getCountryFromPhoneCode(phoneNumber.countryCode)?.isoCode ?: ""
                    this.number = phoneNumber.nationalNumber.toString()
                }
            }
        }
    }
}
