package com.example.wikipedia

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipedia.models.Search
import com.example.wikipedia.models.WikiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// https://ru.wikipedia.org/w/api.php?action=query&list=search&srsearch=%D0%BC%D0%BE%D1%81%D0%BA%D0%B2%D0%B0&utf8=&format=json
// https://ru.wikipedia.org/w/api.php?action=query&prop=revisions&titles=%D0%9C%D0%BE%D1%81%D0%BA%D0%B2%D0%B0%20(%D0%B3%D0%BE%D1%81%D1%82%D0%B8%D0%BD%D0%B8%D1%86%D0%B0,%20%D0%9C%D0%BE%D1%81%D0%BA%D0%B2%D0%B0)&rvslots=*&rvprop=content&formatversion=2&format=json


/**
 * Исследуем и составляем план.
 * API
 *
 * EditText, Search.
 *      HttpUrlConnection... ????
 *      Retrofit
 *      JSON / GSON
 * Result List / Empty View
 * DetailActivity
 *

 */
class MainActivity : AppCompatActivity() {

    private val searchInput: EditText by lazy {
        findViewById(R.id.search)
    }

    private val resultList: RecyclerView by lazy {
        findViewById(R.id.list)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ru.wikipedia.org/w/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val adapter = WikiAdapter()
        resultList.adapter = adapter
        adapter.clickListener = { search ->
            val intent = Intent(this, WikiPageActivity::class.java)
            intent.putExtra(WIKI_TITLE, search.title)
            startActivity(intent)
        }
        resultList.layoutManager = LinearLayoutManager(this)

        searchInput.addTextChangedListener {
            val searchQuery = it?.toString() ?: return@addTextChangedListener
            val wikiService = retrofit.create(WikiService::class.java)

            wikiService.search(query = searchQuery)?.enqueue(object : Callback<WikiResponse?> {
                override fun onResponse(call: Call<WikiResponse?>, response: Response<WikiResponse?>) {
                    adapter.list = response.body()?.query?.search ?: emptyList<Search>()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, response.body().toString(), Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "${response.code()}, ${response.body().toString()}")

                }

                override fun onFailure(call: Call<WikiResponse?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val WIKI_TITLE = "MainActivity"
    }
}


class WikiAdapter() :
    RecyclerView.Adapter<WikiAdapter.WikiViewHolder>() {

    var clickListener: (search: Search) -> Unit = { search -> }

    class WikiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val text: TextView

        init {
            title = view.findViewById(R.id.title)
            text = view.findViewById(R.id.text)
        }
    }

    var list = listOf<Search>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WikiAdapter.WikiViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_item, viewGroup, false)

        return WikiViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: WikiAdapter.WikiViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = list[position].title
        viewHolder.text.text = Html.fromHtml(list[position].snippet)
        viewHolder.itemView.setOnClickListener {
            clickListener(list[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.size

}
