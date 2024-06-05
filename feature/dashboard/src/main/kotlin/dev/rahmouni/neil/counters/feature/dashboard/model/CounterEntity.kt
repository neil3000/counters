package dev.rahmouni.neil.counters.feature.dashboard.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.firebase.Timestamp
import dev.rahmouni.neil.counters.core.data.model.CounterRawData
import dev.rahmouni.neil.counters.feature.dashboard.R

internal data class CounterEntity(
    val uid: String,
    val createdAt: Timestamp,
    val currentValue: Long,
    private val title: String?,
) {
    @Composable
    fun getTitle(): String =
        title ?: stringResource(R.string.feature_dashboard_counterEntity_defaultTitle)
}

internal fun CounterRawData.toEntity(): CounterEntity {
    if (uid == null) throw IllegalStateException("Attempted to convert a CounterRawData with null uid to a CounterEntity")

    return CounterEntity(
        uid = uid!!,
        createdAt = createdAt,
        currentValue = currentValue,
        title = title,
    )
}