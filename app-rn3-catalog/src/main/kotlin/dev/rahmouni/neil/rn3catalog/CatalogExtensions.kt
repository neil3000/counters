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