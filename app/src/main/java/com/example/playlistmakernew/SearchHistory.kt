package com.example.playlistmakernew

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class SearchHistory(val sharedPrefs: SharedPreferences) {

    fun addTrackToHistory(track: Track) {
        updateLocalTrackHistory()
        if (tracksHistoryList.size == 0) {
            tracksHistoryList.add(0, track)
        } else {
            var flag = false
            for (i in tracksHistoryList.indices) {
                if (tracksHistoryList[i].trackId == track.trackId) {
                    flag = true
                }
            }
            if (flag) {
                tracksHistoryList.remove(track)
            }

            tracksHistoryList.add(0, track)

            if (tracksHistoryList.size > TRACKS_HISTORY_SIZE) {
                tracksHistoryList.removeAt(tracksHistoryList.size - 1)
            }
        }
        sharedPrefs.edit().remove(TRACKS_HISTORY_KEY).apply()
        sharedPrefs.edit().putString(TRACKS_HISTORY_KEY, Gson().toJson(tracksHistoryList)).apply()
    }

    fun updateLocalTrackHistory() {
        var jsonString =
            sharedPrefs.getString(TRACKS_HISTORY_KEY, Gson().toJson(emptyArray<Track>()))
        tracksHistoryList.clear()
        //tracksHistoryList = Gson().fromJson(jsonString, Array<Track>::class.java).toMutableList()
        val typeToken = object : TypeToken<MutableList<Track>>() {}.type
        tracksHistoryList = Gson().fromJson(jsonString, typeToken)
    }

    fun clearSearchHistory() {
        sharedPrefs.edit().remove(TRACKS_HISTORY_KEY).apply()
        updateLocalTrackHistory()
    }

    companion object {
        val TRACKS_HISTORY_SIZE = 10
        var tracksHistoryList = mutableListOf<Track>()
    }
}