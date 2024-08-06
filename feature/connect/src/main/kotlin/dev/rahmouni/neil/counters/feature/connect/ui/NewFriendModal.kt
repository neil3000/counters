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

package dev.rahmouni.neil.counters.feature.connect.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.rahmouni.neil.counters.core.data.model.FriendRawData
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3OutlinedTextField
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3AdditionalBigPadding
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rebased.Rn3PhoneForm
import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.feature.connect.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun newFriendModal(onAddFriend: (friendRawData: FriendRawData) -> Unit): () -> Unit {
    val haptic = getHaptic()
    val scope = rememberCoroutineScope()

    var openBottomSheet by rememberSaveable { mutableStateOf(value = false) }
    var hasUserInteracted by rememberSaveable { mutableStateOf(value = false) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var phone by remember { mutableStateOf(PhoneNumber())}

    val phoneUtil = PhoneNumberUtil.getInstance();

    var name by rememberSaveable { mutableStateOf("") }

    fun hide() {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                openBottomSheet = false
                name = ""
                phone = PhoneNumber()
                hasUserInteracted = false
            }
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                openBottomSheet = false
                name = ""
                phone = PhoneNumber()
                hasUserInteracted = false
                },
            sheetState = bottomSheetState,
        ) {
            Column(modifier = Modifier.padding(Rn3AdditionalBigPadding.paddingValues.only(HORIZONTAL))) {
                Rn3OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.feature_connect_newFriendModal_name_label)) },
                    hasUserInteracted = hasUserInteracted,
                    beEmpty = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next)
                )

                Rn3PhoneForm(
                    phone = phone,
                    onPhoneChanged = { updatedPhoneNumber -> phone = updatedPhoneNumber },
                    hasUserInteracted = hasUserInteracted,
                )
            }
            Button(
                onClick = {
                    haptic.click()
                    hasUserInteracted = true
                    if (phoneUtil.isValidNumber(phone)) {
                        hide()
                        onAddFriend(
                            FriendRawData(
                                name = name,
                                phoneCode = Country.getCountryFromPhoneCode(phone.countryCode)?.isoCode,
                                phoneNumber = phone.nationalNumber.toString(),
                                nearby = true,
                            ),
                        )
                    }
                },
                modifier = Modifier.padding(
                    Rn3SurfaceDefaults.paddingValues.copy(top = 4.dp, bottom = 6.dp)).fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.feature_connect_newFriendModal_Button))
            }
        }
    }

    return { openBottomSheet = true  }
}
