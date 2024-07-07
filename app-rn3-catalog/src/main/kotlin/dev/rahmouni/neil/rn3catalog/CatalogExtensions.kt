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
