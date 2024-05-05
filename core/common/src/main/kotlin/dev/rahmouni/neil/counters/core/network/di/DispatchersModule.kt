package dev.rahmouni.neil.counters.core.network.di

import dev.rahmouni.neil.counters.core.network.Dispatcher
import dev.rahmouni.neil.counters.core.network.Rn3Dispatchers.Default
import dev.rahmouni.neil.counters.core.network.Rn3Dispatchers.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
