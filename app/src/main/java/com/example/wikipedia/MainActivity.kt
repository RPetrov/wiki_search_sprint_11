package com.example.wikipedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipedia.models.Models
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

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

    private val searchView: EditText by lazy {
        findViewById<EditText>(R.id.search)
    }

    private val list: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ru.wikipedia.org/w/api.php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        val service = retrofit.create(WikiService::class.java)

        searchView.addTextChangedListener {
            service.search(srsearch = "Москва")?.enqueue(object : Callback<Models?> {
                override fun onResponse(call: Call<Models?>, response: Response<Models?>) {
                    list.adapter = CustomAdapter(response.body()?.query?.search?.map { it.snippet } ?: emptyList<String>())

                    response.body()?.query?.search?.forEach{
                        println(it)
                    }
                }

                override fun onFailure(call: Call<Models?>, t: Throwable) {
                    println(t)
                }
            })
        }
    }
}


class CustomAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val snippet: WebView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.text)
            snippet = view.findViewById(R.id.snippet)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = Html.fromHtml(dataSet[position])
        //viewHolder.snippet.conte
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
