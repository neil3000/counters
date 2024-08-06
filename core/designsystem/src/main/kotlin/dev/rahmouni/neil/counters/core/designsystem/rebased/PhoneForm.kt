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

package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
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
) {
    var isFocusedPhoneCode by rememberSaveable { mutableStateOf(value = false) }
    var showFullLabelPhoneCode by rememberSaveable { mutableStateOf(value = false) }

    val phoneUtil = PhoneNumberUtil.getInstance()
    var formatter = phoneUtil.getAsYouTypeFormatter(Country.getCountryFromPhoneCode(phone.countryCode)?.isoCode ?: "US")

    var formattedText by remember { mutableStateOf("") }
    var rawInput by remember { mutableStateOf("") }

    LaunchedEffect(phone.countryCode) {
        formatter = phoneUtil.getAsYouTypeFormatter(Country.getCountryFromPhoneCode(phone.countryCode)?.isoCode ?: "US")
        formattedText = rawInput.fold("") { acc, digit ->
            formatter.inputDigit(digit).toString()
        }
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
            label = { Text(text = if (showFullLabelPhoneCode || phone.countryCode != 0) "Phone Code" else "Code") },
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
                }},
            setIsFocused = { isFocused -> isFocusedPhoneCode = isFocused },
            hasUserInteracted = hasUserInteracted,
            enableAutofill = true,
            autofill = AutofillType.PhoneCountryCode,
            beEmpty = beEmpty,
        ) { phoneCode ->
            onPhoneChanged(
                phone.setCountryCode(phoneCode.phoneCode)
            )
        }

        Rn3OutlinedTextField(
            modifier = Modifier.weight(1.5f),
            value = formattedText,
            onValueChange = { newValue ->
                val newDigitsOnly = newValue.filter { it.isDigit() }
                if (newDigitsOnly != rawInput) {
                    formatter.clear()
                    formattedText = if (newDigitsOnly.isEmpty()) {
                        ""
                    } else {
                        newDigitsOnly.fold("") { _, digit ->
                            formatter.inputDigit(digit).toString()
                        }
                    }
                    rawInput = newDigitsOnly
                    onPhoneChanged(phone.setNationalNumber(newDigitsOnly.toLongOrNull() ?: 0L))
                }
            },
            hasUserInteracted = hasUserInteracted,
            maxCharacters = 20,
            label = { Text(text = "Phone number") },
            singleLine = true,
            enableAutofill = true,
            beEmpty = beEmpty,
            autofillTypes = AutofillType.PhoneNumber,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, capitalization = KeyboardCapitalization.Sentences)
        )
    }
}
