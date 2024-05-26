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

package dev.rahmouni.neil.counters.feature.dashboard.bottomSheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.feature.dashboard.bottomSheet.pages.AddCounterNamePage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddCounterBottomSheet(
    navController: NavController,
    pageDef: String,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            navController.popBackStack()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = Color.Transparent,
    ) {
        Column {
            Box {
                var page by rememberSaveable { mutableStateOf(pageDef) }

                AnimatedContent(
                    targetState = page,
                    transitionSpec = {
                        (expandVertically { height -> height } + fadeIn()).togetherWith(
                            shrinkVertically { height -> height } + fadeOut(),
                        )
                            .using(SizeTransform(clip = false))
                    },
                    label = "animatedPage",
                ) { screen ->
                    when (screen) {
                        "name" -> AddCounterNamePage(navController)
                    }
                }
            }
        }
    }
}