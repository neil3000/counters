package rahmouni.neil.counters.settings

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    val preferences: SharedPreferences =
        context.getSharedPreferences("RahNeil_N3:Counters", Context.MODE_PRIVATE)

    var debugMode: Boolean
        get() = preferences.getBoolean("DEBUG_MODE", false)
        set(value) = preferences.edit().putBoolean("DEBUG_MODE", value).apply()

    var startWeekDay: StartWeekDay
        get() = StartWeekDay.values()[preferences.getInt(
            "START_WEEK_DAY",
            StartWeekDay.LOCALE.ordinal
        )]
        set(value) = preferences.edit().putInt("START_WEEK_DAY", value.ordinal).apply()

    var weekDisplay: WeekDisplay
        get() = WeekDisplay.values()[preferences.getInt("WEEK_DISPLAY", WeekDisplay.NUMBER.ordinal)]
        set(value) = preferences.edit().putInt("WEEK_DISPLAY", value.ordinal).apply()

    var analyticsEnabled: Boolean
        get() = preferences.getBoolean("ANALYTICS", true)
        set(value) = preferences.edit().putBoolean("ANALYTICS", value).apply()

    var crashlyticsEnabled: Boolean
        get() = preferences.getBoolean("CRASHLYTICS", true)
        set(value) = preferences.edit().putBoolean("CRASHLYTICS", value).apply()

    // App start = 3
    // Add increment = 1
    var tipsStatus: Int
        get() = preferences.getInt("TIPS_STATUS", 0)
        set(value) = preferences.edit().putInt("TIPS_STATUS", value).apply()

    // Accessibility
    var iconSwitchesEnabled: Boolean
        get() = preferences.getBoolean("ICON_SWITCHES", false)
        set(value) = preferences.edit().putBoolean("ICON_SWITCHES", value).apply()

    var confettiDisabled: Boolean
        get() = preferences.getBoolean("CONFETTI_DISABLED", false)
        set(value) = preferences.edit().putBoolean("CONFETTI_DISABLED", value).apply()
}