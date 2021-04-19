package com.daee.dummies

import com.daee.dummies.api.Sampling
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {
    @GET("/random_identity")
    fun getSubscriber():Call<Sampling>
}