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

package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

@Composable
fun Rn3LazyRowWithPadding(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 8.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: LazyListScope.() -> Unit,
) {
    LazyRow(
        modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        rn3Spacer(horizontalPadding)
        content()
        rn3Spacer(horizontalPadding)
    }
}

private fun LazyListScope.rn3Spacer(horizontalPadding: Dp) {
    item {
        Spacer(modifier = Modifier.size(horizontalPadding, 0.dp))
    }
}

@Composable
@Rn3PreviewComponentDefault
private fun Default() {
    Rn3Theme {
        Rn3LazyRowWithPadding {
            repeat(10) {
                item {
                    Rn3Switch(checked = it % 2 == 0, contentDescription = null) {}
                }
            }
        }
    }
}
