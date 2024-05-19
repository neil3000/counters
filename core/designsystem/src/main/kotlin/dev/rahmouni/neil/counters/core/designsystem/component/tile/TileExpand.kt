package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

@Composable
fun Rn3TileExpand(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit,
) {
    val haptic = getHaptic()

    var expanded by rememberSaveable { mutableStateOf(false) }
    val degreeAnimation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron animation",
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(
            Modifier.toggleable(
                value = expanded,
                onValueChange = {
                    haptic.toggle(it)
                    expanded = it
                },
                role = Role.DropdownList,
            ),
        ) {
            Row(
                Modifier.padding(16.dp),
                horizontalArrangement = spacedBy(16.dp),
            ) {
                Icon(imageVector = icon, null)
                Text(title)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Outlined.ExpandMore, null, Modifier.rotate(degreeAnimation))
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                content()
            }
        }
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileExpand(
                title = "Title",
                icon = Outlined.Info,
            ) {
                Text(
                    text = "Lorem ipsum blag blag blah MEZKNGvmzls,gMRKSWFNbwmdfl,bwDLFnhbl!kwd:f;bwdfb",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }
}