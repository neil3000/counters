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