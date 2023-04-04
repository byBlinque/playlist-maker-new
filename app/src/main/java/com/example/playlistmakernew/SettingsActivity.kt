package com.example.playlistmakernew

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmakernew.App.Companion.isDarkThemeOn

import com.google.android.material.switchmaterial.SwitchMaterial
import org.w3c.dom.Text


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        if (sharedPrefs.getString(NIGHT_THEME_KEY, null) != null) {
            themeSwitcher.setChecked(sharedPrefs.getString(NIGHT_THEME_KEY, null).toBoolean())
        } else {
            themeSwitcher.setChecked(isDarkThemeOn())
        }
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putString(NIGHT_THEME_KEY, checked.toString()).apply()
        }


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
            intent.data =
                Uri.parse("mailto:" + getString(R.string.settings_support_intent_email_adress))
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.settings_support_intent_email_subject)
            )
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.settings_support_intent_email_text)
            )
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