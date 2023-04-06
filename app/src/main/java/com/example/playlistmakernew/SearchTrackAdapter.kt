package com.example.playlistmakernew

import android.app.Application
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

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