package com.daee.dummies

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.daee.dummies.api.Sampling
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var itemAdapter: ListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var page = 1
    private var per_page = 10
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemAdapter = ListAdapter(mutableListOf())
        layoutManager = LinearLayoutManager(this)

        rv_list.adapter = itemAdapter
        rv_list.layoutManager = layoutManager

        getPage()

        rv_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = itemAdapter.itemCount

                if (!isLoading) {

                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        page++
                        getPage()
                    }

                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getPage(){
        isLoading = true
        progressBar.visibility = View.VISIBLE
        val start: Int = (page - 1)*per_page
        val end: Int = page * per_page

        for(i:Int in start..end){
            getFakeSubs()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (::itemAdapter.isInitialized) {
                itemAdapter.notifyDataSetChanged()
            } else {
                itemAdapter= ListAdapter(mutableListOf())
                rv_list.adapter = itemAdapter
            }
            isLoading = false
            progressBar.visibility = View.GONE
        }, 5000)
    }

    private fun getFakeSubs(){
        var newDataSampling: Sampling?
        val api = Retrofit.Builder()
                .baseUrl("http://apigenidentity.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)


        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getSubscriber().awaitResponse()
            if(response.isSuccessful){
                val data = response.body()!!
                    withContext(Dispatchers.Main){
                       newDataSampling = Sampling(
                               data.address,
                               data.age,
                               data.company,
                               data.gender,
                               data.image_url,
                               data.name,
                               data.sport
                       )
                        itemAdapter.addSubs(newDataSampling!!)
                    }
                }
            }
        }
    }


/*
load_text.setOnClickListener{
            val api = Retrofit.Builder()
                    .baseUrl("http://apigenidentity.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiRequests::class.java)


            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getSubscriber().awaitResponse()
                if(response.isSuccessful){
                    val data = response.body()!!
                    withContext(Dispatchers.Main){
                        name.text = data.name
                        Glide.with(this@MainActivity)
                            .load(data.image_url)
                            .placeholder(R.drawable.loading_spinner)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(avatar)
                    }
                }
            }
        }
 */