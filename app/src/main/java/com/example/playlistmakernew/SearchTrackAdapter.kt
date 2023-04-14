package com.example.playlistmakernew

import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.Serializable

class SearchTrackAdapter(
    private val tracks: MutableList<Track>,
    private val sharedPrefs: SharedPreferences
) : RecyclerView.Adapter<SearchTrackViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            SearchHistory(sharedPrefs).addTrackToHistory(tracks[position])
            val context = holder.itemView.context
            val audioPlayerIntent = Intent(context, AudioPlayerActivity::class.java)
            audioPlayerIntent.putExtra("selectedTrack",Gson().toJson(SearchHistory.tracksHistoryList[0]))
            context.startActivity(audioPlayerIntent)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateAdapter(newItems: MutableList<Track>) {
        tracks.clear()
        tracks.addAll(newItems)
        this.notifyDataSetChanged()
    }

}