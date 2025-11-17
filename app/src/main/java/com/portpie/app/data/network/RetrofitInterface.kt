package com.portpie.study_kotlin.data.network

import com.portpie.app.data.network.UpbitApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInterface {
//    private const val BASE_URL = "https://api.coingecko.com/api/v3/"
private const val BASE_URL = "https://api.upbit.com/v1/"
    val api: UpbitApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UpbitApi::class.java)
    }
}