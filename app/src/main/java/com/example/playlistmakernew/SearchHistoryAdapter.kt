package com.example.playlistmakernew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(
    private val tracks: MutableList<Track>
) : RecyclerView.Adapter<SearchTrackViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(tracks[position])
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