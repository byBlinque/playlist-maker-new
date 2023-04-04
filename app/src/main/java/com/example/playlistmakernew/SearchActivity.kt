package com.example.playlistmakernew

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import retrofit2.*

import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_EDIT_TEXT = "SEARCH_EDIT_TEXT"
    }

    var searchText: String = ""
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesAPI::class.java)

    var lastRequest: String = ""
    var trackArray = mutableListOf<Track>()

    lateinit var nothingFoundLight: ImageView
    lateinit var nothingFoundDark: ImageView
    lateinit var nothingFoundTV: TextView
    lateinit var internetConnectionLight: ImageView
    lateinit var internetConnectionDark: ImageView
    lateinit var internetConnectionTV: TextView
    lateinit var refreshBtn: MaterialCardView
    lateinit var searchTrackAdapter: SearchTrackAdapter
    lateinit var searchET: EditText
    lateinit var searchHistoryTV: TextView
    lateinit var searchHistoryRV: RecyclerView
    lateinit var clearHistoryBtn: MaterialCardView
    lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        val clearButton = findViewById<ImageView>(R.id.clear_btn)
        val backButton = findViewById<ImageView>(R.id.back_btn)
        val searchTrackRV: RecyclerView = findViewById(R.id.search_ac_rv)
        searchET = findViewById<EditText>(R.id.search_et)
        nothingFoundLight = findViewById(R.id.nothing_found_light)
        nothingFoundDark = findViewById(R.id.nothing_found_dark)
        nothingFoundTV = findViewById(R.id.nothing_found_tv)
        internetConnectionLight = findViewById(R.id.internet_connection_light)
        internetConnectionDark = findViewById(R.id.internet_connection_dark)
        internetConnectionTV = findViewById(R.id.internet_connection_tv)
        refreshBtn = findViewById(R.id.refresh_btn)
        searchHistoryTV = findViewById(R.id.search_history_tv)
        searchHistoryRV = findViewById(R.id.search_history_rv)
        clearHistoryBtn = findViewById(R.id.clear_history_btn)

        //sharedPrefs.edit().clear().apply()
        SearchHistory(sharedPrefs).getTracksHistory()

        searchET.setText(searchText)

        searchTrackRV.layoutManager = LinearLayoutManager(this)
        searchTrackAdapter = SearchTrackAdapter(trackArray, sharedPrefs)
        searchTrackRV.adapter = searchTrackAdapter

        searchHistoryRV.layoutManager = LinearLayoutManager(this)
        searchHistoryAdapter = SearchHistoryAdapter(SearchHistory.tracksHistoryList)
        searchHistoryRV.adapter = searchHistoryAdapter

        sharedPrefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            //Log.d("LISTENER", "Пришли")
            searchHistoryAdapter.updateAdapter(SearchHistory.tracksHistoryList)
        }

        clearHistoryBtn.setOnClickListener {
            //trackHistoryArray.clear()
            //searchHistoryAdapter.notifyDataSetChanged()
        }

        clearButton.setOnClickListener {
            searchET.setText("")
            val view: View? = this.currentFocus
            if (view != null) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
            clearAdapter()
            turnOffErrors()
        }

        backButton.setOnClickListener {
            finish()
        }

        searchET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request(searchET.text.toString())
                true
            }
            false
        }

        refreshBtn.setOnClickListener {
            request(lastRequest)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
        searchET.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_EDIT_TEXT, searchText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        searchText = savedInstanceState.getString(SEARCH_EDIT_TEXT, "")
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    private fun turnOffErrors() {
        nothingFoundTV.visibility = View.GONE
        nothingFoundLight.visibility = View.GONE
        nothingFoundDark.visibility = View.GONE
        internetConnectionLight.visibility = View.GONE
        internetConnectionDark.visibility = View.GONE
        internetConnectionTV.visibility = View.GONE
        refreshBtn.visibility = View.GONE
    }

    private fun hideSearchHistory() {
        searchHistoryTV.visibility = View.GONE
        searchHistoryRV.visibility = View.GONE
        clearHistoryBtn.visibility = View.GONE
    }

    private fun showSearchHistory() {
        searchHistoryTV.visibility = View.VISIBLE
        searchHistoryRV.visibility = View.VISIBLE
        clearHistoryBtn.visibility = View.VISIBLE
    }

    private fun clearAdapter() {
        trackArray.clear()
        searchTrackAdapter.notifyDataSetChanged()
    }

    private fun request(text: String) {
        iTunesService.search(text)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        trackArray.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            turnOffErrors()
                            lastRequest = searchET.text.toString()

                            trackArray.addAll(response.body()?.results!!)
                            searchTrackAdapter.notifyDataSetChanged()
                        }
                        else {
                            turnOffErrors()
                            if (isUsingNightModeResources()) {
                                nothingFoundDark.visibility = View.VISIBLE
                            }
                            else {
                                nothingFoundLight.visibility = View.VISIBLE
                            }
                            nothingFoundTV.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    turnOffErrors()
                    clearAdapter()
                    if (isUsingNightModeResources()) {
                        internetConnectionDark.visibility = View.VISIBLE
                    }
                    else {
                        internetConnectionLight.visibility = View.VISIBLE
                    }
                    internetConnectionTV.visibility = View.VISIBLE
                    refreshBtn.visibility = View.VISIBLE
                }

            })
    }


}