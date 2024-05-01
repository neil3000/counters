package dev.rahmouni.neil.counters.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.rahmouni.neil.counters.core.ui.TrackDisposableJank
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberCountersAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
): CountersAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        windowSizeClass,
    ) {
        CountersAppState(
            navController = navController,
            windowSizeClass = windowSizeClass,
        )
    }
}

@Stable
class CountersAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
