package com.example.playlistmakernew

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    var trackId: Int,
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") var trackTime: String, // Продолжительность трека
    var artworkUrl100: String, // Ссылка на изображение обложки
    var collectionName: String?,
    var releaseDate: String,
    var primaryGenreName: String,
    var country: String
) {
    fun getCoverArtwork(): String = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

}
