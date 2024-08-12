package dev.rahmouni.neil.counters.feature.publication.model

import java.util.Locale

enum class AnalyseType(val text: (String, String) -> String) {
    SUCCESS(text = { x, y -> "$x - $y" }),
    NOTADAPTED(text = { _, _ -> "Your post isn't adapted for our platform" }),
    NEEDPICTURE(text = { _, _ -> "Please add a picture" }),
    NEEDPHONE(text = { _, _ -> "You need to add your phone number to post this" });

    companion object {
        fun fromString(value: String?): AnalyseType {
            return when (value?.uppercase(Locale.ROOT)) {
                "SUCCESS" -> SUCCESS
                "NOTADAPTED" -> NOTADAPTED
                "NEEDPICTURE" -> NEEDPICTURE
                "NEEDPHONE" -> NEEDPHONE
                else -> NOTADAPTED
            }
        }
    }
}