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

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BottomBarItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit,
    var selectedIconColor: Color? = null,
    var unselectedIconColor: Color? = null,
    val selected: Boolean = false,
    val enabled: Boolean = true,
    val fullSize: Boolean = false,
) {
    @Composable
    fun Icon() {
        if (fullSize) {
            Icon(
                imageVector = this.icon,
                contentDescription = this.label,
                modifier = Modifier.size(50.dp),
            )
        } else {
            Icon(imageVector = this.icon, contentDescription = null)
        }
    }

    @Composable
    fun colors(): NavigationBarItemColors {
        return NavigationBarItemDefaults.colors(
            selectedIconColor = selectedIconColor ?: Color.Unspecified,
            unselectedIconColor = unselectedIconColor ?: Color.Unspecified,
        )
    }
}
