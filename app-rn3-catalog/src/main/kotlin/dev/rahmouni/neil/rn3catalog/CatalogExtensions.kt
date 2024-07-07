/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.rn3catalog

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

fun LazyListScope.itemWithToast(content: @Composable (action: () -> Unit) -> Unit) {
    item {
        val context = LocalContext.current

        content {
            Toast.makeText(context, "Action!", Toast.LENGTH_SHORT).show()
        }
    }
}

fun LazyListScope.itemWithBoolean(content: @Composable (value: Boolean, toggleValue: (Any) -> Unit) -> Unit) {
    item {
        var value by remember { mutableStateOf(true) }

        content(value) {
            value = !value
        }
    }
}
