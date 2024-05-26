/*
 * Copyright 2024 Rahmouni Neïl
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

package dev.rahmouni.neil.counters.feature.dashboard.bottomSheet.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.feature.dashboard.model.DashboardViewModel

@Composable
internal fun AddCounterNamePage(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    var currentName by rememberSaveable { mutableStateOf("") }
    var isDuplicate by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.dashboardData

    val haptic = getHaptic()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(currentName) {
        isDuplicate = uiState.dashboardData.counters.any { it.title == currentName }
    }

    Column {
        TextField(
            value = currentName,
            onValueChange = { currentName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .focusRequester(focusRequester),
            label = {
                if (isDuplicate) {
                    Text(text = stringResource(dev.rahmouni.neil.counters.feature.dashboard.R.string.feature_dashboard_bottomSheet_textField_error_label))
                } else {
                    Text(text = stringResource(dev.rahmouni.neil.counters.feature.dashboard.R.string.feature_dashboard_bottomSheet_textField_label))
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!isDuplicate) {
                        viewModel.createUserCounter(currentName)
                        navController.popBackStack()
                    }
                },
            ),
            isError = isDuplicate,
        )
        Button(
            onClick = {
                if (!isDuplicate) {
                    haptic.click()
                    viewModel.createUserCounter(currentName)
                    navController.popBackStack()
                }
            },
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp),
            enabled = currentName.isNotBlank() && !isDuplicate,
        ) {
            Text(stringResource(dev.rahmouni.neil.counters.feature.dashboard.R.string.feature_dashboard_bottomSheet_continueButton_title))
        }
    }
}
