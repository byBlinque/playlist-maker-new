package com.example.playlistmakernew

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

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
            val intent: Intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_intent_text))
            startActivity(intent)
        }
        val settingsAcSupport: RelativeLayout = findViewById(R.id.settings_ac_support)
        settingsAcSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:" + getString(R.string.settings_support_intent_email_adress))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_support_intent_email_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_support_intent_email_text))
            startActivity(intent)
        }
        val settingsAcAgreement: RelativeLayout = findViewById(R.id.settings_ac_agreement)
        settingsAcAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(getString(R.string.settings_agreement)))
            startActivity(intent)
        }
    }
}