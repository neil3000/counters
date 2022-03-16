package rahmouni.neil.counters.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.ui.theme.CountersTheme

@Composable
fun FullscreenDynamicSVG(
    svgRes: Int,
    stringRes: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Icon(
            painterResource(id = svgRes),
            null,
            Modifier
                .fillMaxWidth(.5f),
            Color.Unspecified
        )
        Text(
            stringResource(stringRes),
            Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}