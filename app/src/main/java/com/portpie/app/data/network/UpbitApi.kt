package com.portpie.app.data.network

import com.portpie.app.data.model.UpbitTicker
import retrofit2.http.GET
import retrofit2.http.Query



interface UpbitApi {

    @GET("ticker")
    suspend fun getTicker(
        @Query("markets")markets: String = "KRW-BTC,KRW-ETH,KRW-XPR"
    ):List<UpbitTicker>
}