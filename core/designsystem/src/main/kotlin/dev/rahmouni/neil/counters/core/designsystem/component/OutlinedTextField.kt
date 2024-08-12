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

package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length),
            ),
        )
    }

    Rn3OutlinedTextField(
        modifier = modifier,
        readOnly = readOnly,
        value = textFieldValue,
        trailingIcon = trailingIcon,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onValueChange(newValue.text)
        },
        maxCharacters = maxCharacters,
        label = label,
        singleLine = singleLine,
        beEmpty = beEmpty,
        hasUserInteracted = hasUserInteracted,
        onTextChange = onTextChange,
        enableAutofill = enableAutofill,
        colors = colors,
        autofillTypes = autofillTypes,
        keyboardOptions = keyboardOptions,
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Rn3OutlinedTextField(
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    value: TextFieldValue,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (TextFieldValue) -> Unit,
    maxCharacters: Int? = null,
    label: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    beEmpty: Boolean = false,
    hasUserInteracted: Boolean = false,
    onTextChange: (() -> Unit)? = null,
    enableAutofill: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    autofillTypes: AutofillType? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
) {
    var isEmpty by rememberSaveable { mutableStateOf(value.text.isEmpty()) }
    val isTooLarge = maxCharacters?.let { value.text.length >= it } ?: false

    LaunchedEffect(value) {
        isEmpty = value.text.isEmpty()
    }

    OutlinedTextField(
        value = value,
        readOnly = readOnly,
        onValueChange = { text ->
            if (maxCharacters == null || text.text.length <= maxCharacters) {
                onValueChange(text)
                isEmpty = text.text.isEmpty()
                onTextChange?.invoke()
            }
        },
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .autofill(
                autofillTypes = autofillTypes,
                onFill = { filled ->
                    onValueChange(value.copy(text = filled))
                    isEmpty = filled.isEmpty()
                },
                enableAutofill = enableAutofill,
            ),
        label = label,
        isError = isTooLarge || (isEmpty && !beEmpty && hasUserInteracted),
        singleLine = singleLine,
        supportingText = {
            when {
                isTooLarge -> Text(
                    text = stringResource(
                        string.core_designsystem_limitCharactersReached,
                        maxCharacters!!,
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
        keyboardOptions = keyboardOptions,
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
