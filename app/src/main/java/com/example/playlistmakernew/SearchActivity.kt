package com.example.playlistmakernew

import android.content.Context
import android.content.res.Configuration
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
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
    var trackArray: ArrayList<Track> = arrayListOf()

    lateinit var nothingFoundLight: ImageView
    lateinit var nothingFoundDark: ImageView
    lateinit var nothingFoundTV: TextView
    lateinit var internetConnectionLight: ImageView
    lateinit var internetConnectionDark: ImageView
    lateinit var internetConnectionTV: TextView
    lateinit var refreshBtn: MaterialCardView
    lateinit var searchTrackAdapter: SearchTrackAdapter
    lateinit var searchET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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

        searchET.setText(searchText)

        /*val trackArray: ArrayList<Track> = arrayListOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )*/



        searchTrackRV.layoutManager = LinearLayoutManager(this)
        searchTrackAdapter = SearchTrackAdapter(trackArray)
        searchTrackRV.adapter = searchTrackAdapter

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