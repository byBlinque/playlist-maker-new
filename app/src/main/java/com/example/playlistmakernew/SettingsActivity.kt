package com.example.playlistmakernew

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn: ImageView = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }
        val settingsAcShare: RelativeLayout = findViewById(R.id.settings_ac_share)
        settingsAcShare.setOnClickListener {
            val textToSend: String = "https://practicum.yandex.ru/learn/android-developer"
            val intent: Intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, textToSend)
            startActivity(intent)
        }
    }
}