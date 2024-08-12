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

package dev.rahmouni.neil.counters.feature.connect.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3AdditionalPadding
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

    var phone by remember { mutableStateOf(PhoneNumber()) }

    val phoneUtil = PhoneNumberUtil.getInstance()

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
            Column(modifier = Modifier.padding(Rn3AdditionalPadding.paddingValues.only(HORIZONTAL))) {
                Rn3OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.feature_connect_newFriendModal_name_label)) },
                    hasUserInteracted = hasUserInteracted,
                    beEmpty = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    ),
                )

                Rn3PhoneForm(
                    phone = phone,
                    onPhoneChanged = { updatedPhoneNumber -> phone = updatedPhoneNumber },
                    hasUserInteracted = hasUserInteracted,
                    keyboardOptionsNumber = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                modifier = Modifier
                    .padding(Rn3SurfaceDefaults.paddingValues.copy(top = 4.dp, bottom = 12.dp))
                    .fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.feature_connect_newFriendModal_Button))
            }
        }
    }

    return { openBottomSheet = true }
}
