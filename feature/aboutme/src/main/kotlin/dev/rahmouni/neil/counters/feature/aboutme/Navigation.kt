package dev.rahmouni.neil.counters.feature.aboutme

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ABOUT_ME__ROUTE = "aboutMe"

fun NavController.navigateToAboutMe(navOptions: NavOptions? = null) = navigate(
    ABOUT_ME__ROUTE, navOptions,
)

fun NavGraphBuilder.aboutMeScreen(navController: NavController) {
    composable(route = ABOUT_ME__ROUTE) {
        AboutMeRoute(
            onBackIconButtonClicked = { navController.popBackStack() },
        )
    }
}