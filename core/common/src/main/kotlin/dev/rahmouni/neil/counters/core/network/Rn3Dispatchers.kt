package dev.rahmouni.neil.counters.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val rn3Dispatcher: Rn3Dispatchers)

enum class Rn3Dispatchers {
    Default,
    IO,
}
