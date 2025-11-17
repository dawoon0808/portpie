package com.portpie.app.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class GoldRepository {

    suspend fun getGoldSpotPriceKR(): Double = withContext(Dispatchers.IO) {
        val url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EA%B8%88%EC%8B%9C%EC%84%B8&ackey=1g354bht"

        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(5000)
                .get()

            // 순금 1g 가격 선택자
            val element = doc.selectFirst("div.gold_price strong.price")
                ?: return@withContext 0.0


            val priceText = element.text()
                .replace(",", "")
                .trim()
            Log.d("jdw",priceText.toString())
            priceText.toDoubleOrNull() ?: 0.0
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }

}