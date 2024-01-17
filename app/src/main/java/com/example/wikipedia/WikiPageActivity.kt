package com.example.wikipedia

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wikipedia.models.pages.PageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale


class WikiPageActivity : AppCompatActivity() {

    var mTts: TextToSpeech? = null

    private val text: TextView by lazy {
        findViewById(R.id.text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wiki_page)
        val pageTitle = intent.getStringExtra(MainActivity.WIKI_TITLE) ?: "NO_VALUE"
        Log.i(TAG, pageTitle)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://ru.wikipedia.org/w/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        val wikiService = retrofit.create(WikiService::class.java)
        wikiService.getPage(titles = pageTitle)?.enqueue(object : Callback<PageResponse?> {
            override fun onResponse(call: Call<PageResponse?>, response: Response<PageResponse?>) {
                val text = response.body()?.query?.pages?.firstOrNull()?.extract
                mTts = TextToSpeech(
                    this@WikiPageActivity
                ) {
                    mTts?.language = Locale("RU")
                    mTts?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
                }
            }

            override fun onFailure(call: Call<PageResponse?>, t: Throwable) {
                Log.e(TAG, t.message, t)
            }
        })

    }

    override fun onPause() {
        super.onPause()
        mTts?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mTts?.shutdown()
    }

    companion object {
        private const val TAG = "WikiPageActivity"
    }
}