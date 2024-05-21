package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

@Composable
fun ExpandableSurface(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
    expandedContent: @Composable AnimatedVisibilityScope.() -> Unit,
    tonalElevation: Dp = 0.dp,
) {
    val haptic = getHaptic()

    var expanded by rememberSaveable { mutableStateOf(false) }
    val degreeAnimation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron animation",
        animationSpec = tween(easing = EaseOut),
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        tonalElevation = tonalElevation,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            Modifier.toggleable(
                value = expanded,
                onValueChange = {
                    haptic.click()
                    expanded = it
                },
                role = Role.DropdownList,
            ),
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                content()
                Spacer(modifier = Modifier.weight(1f))
                Icon(Outlined.ExpandMore, null, Modifier.rotate(degreeAnimation))
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                expandedContent()
            }
        }
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            ExpandableSurface(
                content = {
                    Icon(Outlined.Info, null)
                    Spacer(Modifier.width(16.dp))
                    Text(text = "Info")
                },
                expandedContent = {
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam nec leo justo. Praesent consequat et tortor sit amet sodales. Praesent pulvinar gravida metus, ac pretium dolor.",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                },
            )
        }
    }
}