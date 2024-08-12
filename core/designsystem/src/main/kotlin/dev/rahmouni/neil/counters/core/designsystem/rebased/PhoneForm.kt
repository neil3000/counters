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

package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.rahmouni.neil.counters.core.designsystem.R
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3OutlinedTextField
import dev.rahmouni.neil.counters.core.model.data.Country
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Rn3PhoneForm(
    phone: PhoneNumber,
    onPhoneChanged: (PhoneNumber) -> Unit,
    hasUserInteracted: Boolean = false,
    beEmpty: Boolean = false,
    keyboardOptionsNumber: KeyboardOptions,
) {
    var isFocusedPhoneCode by rememberSaveable { mutableStateOf(value = false) }
    var showFullLabelPhoneCode by rememberSaveable { mutableStateOf(value = false) }

    val phoneUtil = PhoneNumberUtil.getInstance()
    var formatter = phoneUtil.getAsYouTypeFormatter(
        Country.getCountryFromPhoneCode(phone.countryCode)?.isoCode ?: "US",
    )

    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var rawInput by remember { mutableStateOf("") }

    fun formatPhoneNumber(rawInput: String, countryCode: Int): Pair<String, String> {
        formatter = phoneUtil.getAsYouTypeFormatter(
            Country.getCountryFromPhoneCode(countryCode)?.isoCode ?: "US",
        )
        val formattedNumber = if (rawInput != "0") {
            rawInput.fold("") { acc, digit ->
                formatter.inputDigit(digit).toString()
            }
        } else ""
        return Pair(formattedNumber, rawInput.filter { it.isDigit() })
    }

    LaunchedEffect(key1 = phone.countryCode, key2 = "init") {
        val (formattedNumber, digitsOnly) = formatPhoneNumber(
            phone.nationalNumber.toString(),
            phone.countryCode,
        )
        textFieldValue = TextFieldValue(formattedNumber, TextRange(formattedNumber.length))
        rawInput = digitsOnly
    }

    LaunchedEffect(isFocusedPhoneCode) {
        if (isFocusedPhoneCode) {
            delay(timeMillis = 50)
            showFullLabelPhoneCode = true
        } else {
            showFullLabelPhoneCode = false
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Rn3CountryDropDownMenu(
            modifier = Modifier.weight(1f),
            value = if (phone.countryCode != 0) "+${phone.countryCode}" else "",
            label = {
                Text(
                    text = if (showFullLabelPhoneCode || phone.countryCode != 0) stringResource(
                        R.string.core_designsystem_phoneForm_phoneCode_full,
                    ) else stringResource(R.string.core_designsystem_phoneForm_phoneCode_small),
                )
            },
            textItem = { phoneCode ->
                Row(verticalAlignment = CenterVertically) {
                    Icon(
                        imageVector = phoneCode.icon(),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(text = ("+" + phoneCode.phoneCode.toString()))
                }
            },
            setIsFocused = { isFocused -> isFocusedPhoneCode = isFocused },
            hasUserInteracted = hasUserInteracted,
            enableAutofill = true,
            autofill = AutofillType.PhoneCountryCode,
            onValueChange = { phoneCode ->
                phone.countryCode = phoneCode.phoneCode
                onPhoneChanged(
                    phone.setCountryCode(phoneCode.phoneCode),
                )
            },
            beEmpty = beEmpty,
        )

        Rn3OutlinedTextField(
            modifier = Modifier.weight(1.5f),
            value = textFieldValue,
            onValueChange = { newValue ->
                val newDigitsOnly = newValue.text.filter { it.isDigit() }
                val cursorPosition = newValue.selection.start
                formatter.clear()
                val formattedText = newDigitsOnly.fold("") { _, digit ->
                    formatter.inputDigit(digit).toString()
                }
                val cursorOffset =
                    calculateCursorOffset(newValue.text, formattedText, cursorPosition)
                textFieldValue = TextFieldValue(formattedText, TextRange(cursorOffset))
                rawInput = newDigitsOnly
                onPhoneChanged(phone.setNationalNumber(newDigitsOnly.toLongOrNull() ?: 0L))
            },
            hasUserInteracted = hasUserInteracted,
            maxCharacters = 20,
            label = { Text(text = stringResource(R.string.core_designsystem_phoneForm_phoneNumber)) },
            singleLine = true,
            enableAutofill = true,
            beEmpty = beEmpty,
            autofillTypes = AutofillType.PhoneNumber,
            keyboardOptions = keyboardOptionsNumber,
        )
    }
}

fun calculateCursorOffset(originalText: String, formattedText: String, cursorPosition: Int): Int {
    val originalDigits = originalText.substring(0, cursorPosition).count { it.isDigit() }
    var digitsCounted = 0
    var formattedPosition = 0
    for (ch in formattedText) {
        if (ch.isDigit()) {
            if (digitsCounted == originalDigits) {
                break
            }
            digitsCounted++
        }
        formattedPosition++
    }
    return formattedPosition
}
