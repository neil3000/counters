package dev.rahmouni.neil.counters.core.accessibility

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AccessibilityModule {
    @Binds
    abstract fun bindsAccessibilityHelper(accessibilityHelper: AccessibilityHelper): AccessibilityHelper
}
