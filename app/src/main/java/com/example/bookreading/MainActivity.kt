package com.example.bookreading

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookreading.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        window.statusBarColor=getColor(R.color.white)

        noInterProblemSolve()
        binding!!.reloadButton.setOnClickListener {
            binding!!.progressBar.visibility=View.VISIBLE
            noInterProblemSolve()
        }
    }
    private fun noInterProblemSolve(){
        if(checkForInternet(this@MainActivity)){
            binding!!.listOfBookRecyclerViewFiction.visibility=View.VISIBLE
            binding!!.noInternetConnection.visibility= View.GONE
            GlobalScope.launch(Dispatchers.IO) {
                val dataList = getBooksDetails("https://book-finder1.p.rapidapi.com/api/search?book_type=Fiction")
                withContext(Dispatchers.Main) {
                    setListToAdapter(ArrayList(dataList),ArrayList(dataList))
                }
            }
        }else{
            binding!!.noInternetConnection.visibility= View.VISIBLE
            binding!!.listOfBookRecyclerViewFiction.visibility=View.GONE
            binding!!.progressBar.visibility=View.GONE
        }    }
    private suspend fun getBooksDetails(url: String): List<BookInfo> {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "72897d918amsh3842dc9164d30d0p191005jsnd6faad28a903")
                .addHeader("X-RapidAPI-Host", "book-finder1.p.rapidapi.com")
                .build()

            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body
                if (responseBody != null) {
                    val jsonData = responseBody.string()
                    val jsonObject = JSONObject(jsonData).getJSONArray("results")

                    val listOfBook = mutableListOf<BookInfo>()

                    for (i in 0 until jsonObject.length()) {
                        val bookData = extractBookInfo(jsonObject.get(i).toString())
                        if (bookData != null) {
                            listOfBook.add(bookData)
                        }
                    }

                    listOfBook
                } else {
                    println("Response body is null")
                    emptyList()
                }
            } else {
                println("Request failed with HTTP status code: ${response.code}")
                emptyList()
            }
        }
    }
    private fun extractBookInfo(jsonData: String): BookInfo? {
        try {
            val bookData = JSONObject(jsonData)
            val authorFullName = bookData.getJSONArray("authors").optString(0)
            val bookName = bookData.getString("title")
            val bookId = bookData.getInt("canonical_published_work_id")
            val bookImageURL = bookData.getJSONArray("published_works").getJSONObject(0).getString("cover_art_url")
            val summary = bookData.getString("summary")

            return BookInfo(authorFullName, bookName, bookId, bookImageURL, summary)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }
    private fun setListToAdapter(list: ArrayList<BookInfo>, list2: ArrayList<BookInfo>){

        TheBookListAdapter(list,this@MainActivity)
        val adapter=TheBookListAdapter(list,this@MainActivity)
        val layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
        binding!!.listOfBookRecyclerViewFiction.layoutManager=layoutManager
        binding!!.listOfBookRecyclerViewFiction.adapter=adapter
        binding!!.progressBar.visibility=View.GONE
    }
    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }
}