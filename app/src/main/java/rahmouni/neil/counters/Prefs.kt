package rahmouni.neil.counters

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("RahNeil_N3:Counters", Context.MODE_PRIVATE)

    var debugMode: Boolean
        get() = preferences.getBoolean("DEBUG_MODE", false)
        set(value) = preferences.edit().putBoolean("DEBUG_MODE", value).apply()
}