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

package dev.rahmouni.neil.counters.core.designsystem.roundedCorners

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun Rn3RoundedCornersSurfaceGroup(content: SurfaceGroupScope.() -> Unit) {
    val localScope = SurfaceGroupScope()
    localScope.content()

    Column(verticalArrangement = spacedBy(4.dp)) { //TODO: check spacing
        with(localScope.items) {
            var index = 0
            var invisibleCount = 0

            forEach { item ->
                var shape =
                    Rn3RoundedCorners(Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerInternal)

                if (index == 0) shape += Rn3RoundedCorners(top = Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerExternal - Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerInternal)
                if (index == size - invisibleCount - 1) shape += Rn3RoundedCorners(bottom = Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerExternal - Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerInternal)

                AnimatedVisibility(visible = item.visible) {
                    item.content(shape)
                }

                if (item.visible) index++ else invisibleCount++
            }
        }
    }
}

internal data class SurfaceGroupItem(
    val content: @Composable (Rn3RoundedCorners) -> Unit,
    val visible: Boolean,
)

class SurfaceGroupScope {
    internal val items = mutableListOf<SurfaceGroupItem>()

    fun item(visible: Boolean = true, content: @Composable (Rn3RoundedCorners) -> Unit) {
        items.add(SurfaceGroupItem(content, visible))
    }
}

object Rn3RoundedCornersSurfaceGroupDefaults {
    val roundedCornerInternal = 6.dp
    val roundedCornerExternal = 24.dp
}
