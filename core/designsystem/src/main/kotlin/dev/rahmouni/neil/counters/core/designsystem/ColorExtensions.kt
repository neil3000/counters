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

package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable

val ColorScheme.rn3ErrorContainer
    @Composable
    get() = if (isSystemInDarkTheme()) errorContainer else error

val ColorScheme.rn3Error
    @Composable
    get() = contentColorFor(rn3ErrorContainer)

@Composable
fun ButtonDefaults.rn3ErrorButtonColors() = buttonColors().copy(
    containerColor = MaterialTheme.colorScheme.rn3ErrorContainer,
    contentColor = MaterialTheme.colorScheme.rn3Error,
)
