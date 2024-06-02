package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun String.toRn3FormattedString(): AnnotatedString = buildAnnotatedString {
    split("*").forEachIndexed { index, s ->
        if (index % 2 == 1) {
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = if (isSystemInDarkTheme()) FontWeight.Normal else FontWeight.Bold,
                ),
            ) {
                append(s)
            }
        } else {
            append(s)
        }
    }
}