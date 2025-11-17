package com.portpie.study_kotlin.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("simple/price")
    suspend fun getPrice(
        @Query("ids") ids : String,
        @Query("vs_currencies") vsCurrency: String
    ): Map<String, Map<String, Double>>
}