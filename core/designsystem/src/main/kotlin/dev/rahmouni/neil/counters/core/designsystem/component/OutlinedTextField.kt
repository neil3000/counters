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

package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.designsystem.R.string
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentVariation
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Rn3OutlinedTextField(
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    value: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    maxCharacters: Int? = null,
    label: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    beEmpty: Boolean = false,
    hasUserInteracted: Boolean = false,
    onTextChange: (() -> Unit)? = null,
    enableAutofill: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    autofillTypes: AutofillType? = null,
) {
    var isEmpty by rememberSaveable { mutableStateOf(value.isEmpty()) }
    val isTooLarge = maxCharacters?.let { value.length >= it } ?: false

    OutlinedTextField(
        value = value,
        readOnly = readOnly,
        onValueChange = { text ->
            if (maxCharacters == null || text.length <= maxCharacters) {
                onValueChange(text)
                isEmpty = text.isEmpty()
                onTextChange?.invoke()
            }
        },
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .autofill(
                autofillTypes = autofillTypes,
                onFill = {
                    onValueChange(it)
                    isEmpty = false
                },
                enableAutofill = enableAutofill,
            ),
        label = label,
        isError = isTooLarge || (isEmpty && !beEmpty && hasUserInteracted),
        singleLine = singleLine,
        supportingText = {
            when {
                isTooLarge && maxCharacters != null -> Text(
                    text = stringResource(
                        string.core_designsystem_limitCharactersReached,
                        maxCharacters,
                    ),
                    color = MaterialTheme.colorScheme.error,
                )

                isEmpty && !beEmpty && hasUserInteracted -> Text(
                    text = stringResource(string.core_designsystem_pleaseFillout),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        colors = colors,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
    autofillTypes: AutofillType?,
    onFill: ((String) -> Unit),
    enableAutofill: Boolean = true,
) = if (enableAutofill) {
    composed {
        val types = autofillTypes
            ?: throw IllegalArgumentException("autofillTypes must be provided when enableAutofill is true")

        val autofill = LocalAutofill.current
        val autofillNode = AutofillNode(onFill = onFill, autofillTypes = listOf(types))
        LocalAutofillTree.current += autofillNode

        this
            .onGloballyPositioned {
                autofillNode.boundingBox = it.boundsInWindow()
            }
            .onFocusChanged { focusState ->
                autofill?.run {
                    if (focusState.isFocused) {
                        requestAutofillForNode(autofillNode)
                    } else {
                        cancelAutofillForNode(autofillNode)
                    }
                }
            }
    }
} else {
    this
}

@OptIn(ExperimentalComposeUiApi::class)
@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3OutlinedTextField(
                value = "Sample Text",
                onValueChange = {},
                maxCharacters = 20,
                label = { Text(text = "Label") },
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Rn3PreviewComponentVariation
@Composable
private fun Empty() {
    Rn3Theme {
        Surface {
            Rn3OutlinedTextField(
                value = "",
                onValueChange = {},
                maxCharacters = 20,
                beEmpty = false,
                label = { Text(text = "Label") },
                hasUserInteracted = true,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Rn3PreviewComponentVariation
@Composable
private fun Full() {
    Rn3Theme {
        Surface {
            Rn3OutlinedTextField(
                value = "",
                onValueChange = {},
                maxCharacters = 0,
                beEmpty = false,
                label = { Text("Label") },
                hasUserInteracted = true,
            )
        }
    }
}
