package dev.rahmouni.neil.counters.feature.publication.data

enum class AnalyseType(val text: String) {
    SUCCESS(text = "" ),
    NOTADDAPTED(text = "Your post isn't adapted for our platform"),
    NEEDPICTURE(text = "Please add a picture"),
}