package com.portpie.app.data.repository

import com.portpie.app.data.model.UpbitTicker
import com.portpie.study_kotlin.data.network.RetrofitInterface.api

class CoinRepository {
    suspend fun getPrice(ids:String): UpbitTicker? {
     return api.getTicker(ids).firstOrNull()
    }

}