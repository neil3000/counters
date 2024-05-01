package dev.rahmouni.neil.counters.core.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ConfigModule {
    @Binds
    abstract fun bindsConfigHelper(configHelperImpl: FirebaseConfigHelper): ConfigHelper

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseConfig(): FirebaseRemoteConfig {
            return FirebaseRemoteConfig.getInstance()
        }
    }
}
