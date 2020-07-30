package com.cascade.intouch.network

import com.cascade.intouch.model.CategoryData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

typealias ResponseHandler<T> = (T?, Throwable?) -> Unit

object RequestManager {
    private val service: Service = Retrofit.Builder()
            .baseUrl("http://192.168.1.3:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<Service>(Service::class.java)

    fun categoryData(environment: String, firm: String, handler: ResponseHandler<CategoryData>) {
        service.searchCategories(environment, firm).enqueue(object : Callback<CategoryData> {
            override fun onFailure(call: Call<CategoryData>, t: Throwable) {
                handler(null, t)
            }

            override fun onResponse(call: Call<CategoryData>, response: Response<CategoryData>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        handler(it, null)
                    } ?: run {
                        handler(null, Exception("wrong body"))
                    }
                } else {
                    handler(null, Exception("server error"))
                }
            }
        })
    }
}