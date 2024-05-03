package dev.rahmouni.neil.counters.feature.aboutme

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.feature.aboutme.ui.Biography
import dev.rahmouni.neil.counters.feature.aboutme.ui.LoadingPfp
import dev.rahmouni.neil.counters.feature.aboutme.ui.MainActions
import dev.rahmouni.neil.counters.feature.aboutme.ui.SocialLinks
import kotlinx.coroutines.delay

@Composable
internal fun AboutMeRoute(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit,
) {
    AboutMeScreen(
        modifier,
        onBackIconButtonClicked,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AboutMeScreen(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("About me") },
                navigationIcon = {
                    Rn3IconButton(
                        icon = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "",
                        onClick = onBackIconButtonClicked,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.displayCutout,
    ) { paddingValues ->
        AboutMePanel(paddingValues)
    }
}

@Composable
private fun AboutMePanel(paddingValues: PaddingValues) {

    var finishedLoading by remember { mutableStateOf(false) }
    var finishedLoadingAnimation by remember { mutableStateOf(false) }

    // LOADING LOGIC
    // delay for now
    LaunchedEffect(Unit) {
        delay(400)
        finishedLoading = true
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(visible = finishedLoadingAnimation) {
            Column {
                Spacer(
                    Modifier
                        .padding(paddingValues)
                        .padding(bottom = 48.dp),
                )

                Text(
                    text = "Neïl Rahmouni",
                    Modifier.padding(bottom = 24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit(5f, TextUnitType.Em),
                )
            }
        }

        LoadingPfp({ finishedLoading }, finishedLoadingAnimation) {
            finishedLoadingAnimation = true
        }

        AnimatedVisibility(visible = finishedLoadingAnimation) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item { Biography() }
                item { MainActions() }
                item { SocialLinks() }
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        AboutMeScreen()
    }
}