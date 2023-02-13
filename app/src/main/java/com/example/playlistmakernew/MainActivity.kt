package com.example.playlistmakernew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainSearchBtn: Button = findViewById(R.id.main_search_btn)
        val mainMediatekaBtn: Button = findViewById(R.id.main_mediateka_btn)
        val mainSettingsBtn: Button = findViewById(R.id.main_settings_btn)

        val mainSearchBtnListener: View.OnClickListener = object: View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        mainSearchBtn.setOnClickListener(mainSearchBtnListener)

        mainMediatekaBtn.setOnClickListener {
            val mediatekaIntent = Intent(this@MainActivity, MediatekaActivity::class.java)
            startActivity(mediatekaIntent)
        }
        mainSettingsBtn.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}