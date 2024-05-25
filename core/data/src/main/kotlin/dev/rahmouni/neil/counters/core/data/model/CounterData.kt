package dev.rahmouni.neil.counters.core.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class CounterData(
    @DocumentId val uid: String = "",
    val ownerUserUid: String = "",
    val title: String = "Error",
    val currentValue: Long = 0,
    val currentValueComputedSegment: Timestamp = Timestamp(0, 0),
)

@Suppress("ConstPropertyName")
object CounterDataFields {
    const val uid: String = "uid"
    const val ownerUserUid = "ownerUserUid"
    const val title = "title"
    const val currentValue = "currentValue"
    const val currentValueComputedSegment = "currentValueComputedSegment"
}

@Suppress("ConstPropertyName")
object IncrementDataFields {
    const val uid: String = "uid"
    const val value = "value"
    const val createdAt = "createdAt"
}