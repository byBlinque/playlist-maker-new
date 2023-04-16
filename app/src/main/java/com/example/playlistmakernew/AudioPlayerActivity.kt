package com.example.playlistmakernew

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

    lateinit var trackName: TextView
    lateinit var selectedTrack: Track
    lateinit var artistName: TextView
    lateinit var trackCover: ImageView
    lateinit var backButton: ImageView
    lateinit var playbackProgress: TextView
    lateinit var duration: TextView
    lateinit var album: TextView
    lateinit var releaseDate: TextView
    lateinit var genre: TextView
    lateinit var country: TextView

class AudioPlayerActivity : AppCompatActivity() {
    companion object {
        const val PLAYED_TRACK = "PLAYED_TRACK"
    }

    /*val trackName: TextView = findViewById(R.id.track_name)
    var selectedTrack: Track = Gson().fromJson(intent?.getStringExtra("selectedTrack"), Track::class.java)
    val artistName: TextView = findViewById(R.id.artist_name)
    val trackCover: ImageView = findViewById(R.id.track_cover)
    val backButton: ImageView = findViewById(R.id.back_btn)
    val playbackProgress: TextView = findViewById(R.id.playback_progress)
    val duration: TextView = findViewById(R.id.duration_value)
    val album: TextView = findViewById(R.id.album_value)
    val releaseDate: TextView = findViewById(R.id.release_date_value)
    val genre: TextView = findViewById(R.id.genre_value)
    val country: TextView = findViewById(R.id.country_value)*/

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        trackName = findViewById(R.id.track_name)
        selectedTrack = Gson().fromJson(intent?.getStringExtra("selectedTrack"), Track::class.java)
        artistName = findViewById(R.id.artist_name)
        trackCover = findViewById(R.id.track_cover)
        backButton = findViewById(R.id.back_btn)
        playbackProgress = findViewById(R.id.playback_progress)
        duration = findViewById(R.id.duration_value)
        album = findViewById(R.id.album_value)
        releaseDate = findViewById(R.id.release_date_value)
        genre = findViewById(R.id.genre_value)
        country = findViewById(R.id.country_value)

        trackName.setText(selectedTrack.trackName)
        artistName.setText(selectedTrack.artistName)
        duration.setText(
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(selectedTrack.trackTime.toInt()).toString()
        )
        if (selectedTrack.collectionName != null) {
            album.setText(selectedTrack.collectionName)
        }
        releaseDate.setText(
            OffsetDateTime.parse(selectedTrack.releaseDate).toLocalDate().year.toString()
        )
        genre.setText(selectedTrack.primaryGenreName)
        country.setText(selectedTrack.country)

        Glide.with(this)
            .load(selectedTrack.getCoverArtwork())
            .placeholder(R.drawable.track_poster_placeholder)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.audioplayer_track_cover_corner_size)))
            .into(trackCover)

        backButton.setOnClickListener {
            SearchHistory(sharedPrefs = getSharedPreferences(TRACKS_HISTORY_KEY, MODE_PRIVATE)).updateLocalTrackHistory()
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(PLAYED_TRACK, Gson().toJson(SearchHistory.tracksHistoryList[0]))
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        selectedTrack =
            Gson().fromJson(savedInstanceState.getString(PLAYED_TRACK, null), Track::class.java)
        super.onRestoreInstanceState(savedInstanceState)
    }
}