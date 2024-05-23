package dev.rahmouni.neil.counters.core.data.model

import com.google.firebase.firestore.DocumentId
import java.util.UUID

data class UserCounter(@DocumentId val id: String = UUID.randomUUID().toString(), val title: String = "Title")