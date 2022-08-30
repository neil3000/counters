package rahmouni.neil.counters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.utils.banner.Banner
import rahmouni.neil.counters.utils.booleanLiveData

@Composable
fun LongPressTipBanner() {
    val dismissed = prefs.preferences.booleanLiveData("LONG_PRESS_TIP_DISMISSED", false)
        .observeAsState(true)

    AnimatedVisibility(
        !dismissed.value && prefs.tipsStatus >= 8,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top)
    ) {
        Banner(
            title = stringResource(R.string.longPressTipBanner_banner_title),
            description = stringResource(R.string.longPressTipBanner_banner_description),
            icon = Icons.Outlined.TipsAndUpdates
        ) {}
    }
}