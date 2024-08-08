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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3OutlinedTextField
import dev.rahmouni.neil.counters.core.model.data.Country

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Rn3CountryDropDownMenu(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (Country) -> Unit,
    label: @Composable (() -> Unit)? = null,
    hasUserInteracted: Boolean = false,
    beEmpty: Boolean = false,
    textItem: @Composable (Country) -> Unit,
    setIsFocused: (Boolean) -> Unit,
    autofill: AutofillType? = null,
    enableAutofill: Boolean = false,
) {
    var expanded by remember { mutableStateOf(value = false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        ) {
        Rn3OutlinedTextField(
            readOnly = true,
            value = value,
            onValueChange = {},
            label = label,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    setIsFocused(focusState.isFocused)
                },
            hasUserInteracted = hasUserInteracted,
            enableAutofill = enableAutofill,
            autofillTypes = autofill,
            beEmpty = beEmpty,
            )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            ) {
            Country.sortedCountries().forEach { selected ->
                DropdownMenuItem(
                    text = { textItem(selected) },
                    onClick = {
                        expanded = false
                        onValueChange(selected)
                    },
                )
            }
        }
    }
}
