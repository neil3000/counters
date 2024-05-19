package dev.rahmouni.neil.counters.feature.aboutme.model.data

import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData.LocalImage
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData.RemoteImage

sealed interface PfpData {
    data object LocalImage : PfpData
    data class RemoteImage(val url: String) : PfpData
}

internal fun String.toPfpData(): PfpData {
    return when (this) {
        "LOCAL_IMAGE" -> LocalImage
        else -> RemoteImage(this)
    }
}