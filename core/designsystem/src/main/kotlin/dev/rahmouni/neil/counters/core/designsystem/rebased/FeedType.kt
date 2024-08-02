package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.designsystem.R.string

enum class FeedType(private val resourceId: Int) {
    PUBLIC(string.core_designsystem_feedType_public),
    FRIENDS(string.core_designsystem_feedType_friends),
    EVENTS(string.core_designsystem_feedType_events);

    @Composable
    fun text(): String {
        return stringResource(id = resourceId)
    }
}