package com.cascade.intouch.network

import com.cascade.intouch.model.CategoryData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("intouch/{environment}/{firm}")
    fun searchCategories(@Path("environment") environment: String,
                         @Path("firm") firm: String): Call<CategoryData>
}