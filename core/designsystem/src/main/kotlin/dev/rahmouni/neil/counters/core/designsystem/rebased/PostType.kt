package dev.rahmouni.neil.counters.core.designsystem.rebased

import java.util.Locale

enum class PostType {
    TEXT,
    CONTACT,
    POLL;

    companion object {
        fun fromString(value: String?): PostType {
            return when (value?.uppercase(Locale.ROOT)) {
                "TEXT" -> TEXT
                "CONTACT" -> CONTACT
                "POLL" -> POLL
                else -> TEXT
            }
        }
    }
}