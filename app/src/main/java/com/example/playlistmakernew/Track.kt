package com.example.playlistmakernew

import com.google.gson.annotations.SerializedName

data class Track(
    var trackName: String, // Название композиции
    var artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") var trackTime: String, // Продолжительность трека
    var artworkUrl100: String // Ссылка на изображение обложки
) {

}