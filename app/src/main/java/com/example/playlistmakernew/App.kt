package com.example.playlistmakernew

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES = "shared_preferences"
const val NIGHT_THEME_KEY = "night_theme_key"
const val TRACKS_HISTORY_KEY = "tracks_history_key"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        if (sharedPrefs.getString(NIGHT_THEME_KEY, null) != null) {
            (applicationContext as App).switchTheme(
                sharedPrefs.getString(NIGHT_THEME_KEY, null).toBoolean()
            )
        } else {
            (applicationContext as App).switchTheme(isDarkThemeOn())
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        fun Context.isDarkThemeOn(): Boolean {
            return resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }


}