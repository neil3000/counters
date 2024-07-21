package dev.rahmouni.neil.counters.feature.publication.data

enum class AnalyseType(val text: (String, String) -> String) {
    SUCCESS(text = { x, y -> "$x - $y" }),
    NOTADDAPTED(text = { _, _ -> "Your post isn't adapted for our platform" }),
    NEEDPICTURE(text = { _, _ -> "Please add a picture" }),
}