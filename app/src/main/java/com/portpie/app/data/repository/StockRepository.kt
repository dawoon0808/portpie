package com.portpie.app.data.repository

import android.util.Log
import com.portpie.app.data.model.Stock
import com.portpie.app.data.model.StockType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.jsoup.Jsoup
import java.net.URLEncoder

class StockRepository {

    private var rate : Double? = null

    init {
        // Repository 생성 시 자동으로 한 번만 가져옴
        runBlocking {
            rate = fetchUsdToKrwRateFromInvesting()
        }
    }
    suspend fun searchStockFromInvesting(keyword: String): List<Stock> = withContext(Dispatchers.IO) {
        val url = "https://kr.investing.com/search/?q=${URLEncoder.encode(keyword, "UTF-8")}"
        val result = mutableListOf<Stock>()

        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("https://www.google.com/")
                .timeout(10_000)
                .get()


            val items = doc.select("a.js-inner-all-results-quote-item")
            for (item in items) {
                val name = item.selectFirst("span.third")?.text()?.trim() ?: continue
                val code = item.selectFirst("span.second")?.text()?.trim() ?: ""
                val href = item.attr("href").trim()
                val symbol = href.substringAfterLast("/")
                val marketInfo = item.selectFirst("span.fourth")?.text() ?: ""

                // 국내/해외 구분
                val type = if(marketInfo.contains("인베스팅닷컴")){
                    StockType.CRYPTO
                }else if(marketInfo.contains("상장지수펀드")){
                    if(marketInfo.contains("서울")|| marketInfo.contains("KOSPI") || marketInfo.contains("KOSDAQ")){
                        StockType.ETF_DOMESTIC
                    }else{
                        StockType.ETF_FOREIGN
                    }

                }else if(marketInfo.contains("서울") || marketInfo.contains("KOSPI") || marketInfo.contains("KOSDAQ")){
                    StockType.DOMESTIC
                }else{
                    StockType.FOREIGN
                }

                result.add(
                    Stock(
                        name = name,
                        symbol = symbol,
                        price = 0.0,
                        code = code,
                        type = type
                    )
                )
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getInvestingKREtf(stock: Stock): Stock = withContext(Dispatchers.IO) {
        val url = "https://kr.investing.com/etfs/${stock.symbol}"
        Log.d("jdw",url)
        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("https://www.google.com/")
                .timeout(10_000)
                .get()

            val priceElement = doc.selectFirst("div[data-test=instrument-price-last]")
            val priceText = priceElement?.text()?.replace(",", "")?.trim()

            val price = priceText?.toDoubleOrNull() ?: 0.0

            val type = if (doc.text().contains("서울") || doc.text().contains("KOSPI")) {
                StockType.ETF_DOMESTIC
            } else {
                StockType.ETF_FOREIGN

            }
            var finalPrice = price
            if(type== StockType.ETF_FOREIGN){
                val rate = fetchUsdToKrwRateFromInvesting()
                finalPrice = price * rate
            }
            stock.copy(price = finalPrice, type = type)

        } catch (e: Exception) {
            e.printStackTrace()
            stock.copy(price = 0.0)
        }
    }
    suspend fun getInvestingKREtf(symbol: String,originPrice: Double): Double {
        val url = "https://kr.investing.com/etfs/${symbol}"
        Log.d("jdw",url)
        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("https://www.google.com/")
                .timeout(10_000)
                .get()

            val priceElement = doc.selectFirst("div[data-test=instrument-price-last]")
            val priceText = priceElement?.text()?.replace(",", "")?.trim()

            val price = priceText?.toDoubleOrNull() ?: 0.0

            val type = if (doc.text().contains("서울") || doc.text().contains("KOSPI")) {
                StockType.ETF_DOMESTIC
            } else {
                StockType.ETF_FOREIGN

            }
            var finalPrice = price
            if(type== StockType.ETF_FOREIGN){
                val rate = fetchUsdToKrwRateFromInvesting()
                finalPrice = price * rate
            }
           return finalPrice

        } catch (e: Exception) {
            e.printStackTrace()
           return originPrice
        }
    }
    suspend fun getInvestingKRStock(stock: Stock): Stock = withContext(Dispatchers.IO) {
        val url = "https://kr.investing.com/equities/${stock.symbol}"
        Log.d("jdw",url)
        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("https://www.google.com/")
                .timeout(10_000)
                .get()

            val priceElement = doc.selectFirst("div[data-test=instrument-price-last]")
            val priceText = priceElement?.text()?.replace(",", "")?.trim()

            val price = priceText?.toDoubleOrNull() ?: 0.0

            val type = if (doc.text().contains("서울") || doc.text().contains("KOSPI")) {
                StockType.DOMESTIC
            } else {
                StockType.FOREIGN

            }
            var finalPrice = price
            if(type== StockType.FOREIGN){
                val rate = fetchUsdToKrwRateFromInvesting()
                finalPrice = price * rate
            }
            stock.copy(price = finalPrice, type = type)

        } catch (e: Exception) {
            e.printStackTrace()
            stock.copy(price = 0.0)
        }
    }
    suspend fun getInvestingKRStock(symbol: String,originPrice: Double): Double{
        val url = "https://kr.investing.com/equities/${symbol}"
        Log.d("jdw",url)
        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("https://www.google.com/")
                .timeout(10_000)
                .get()

            val priceElement = doc.selectFirst("div[data-test=instrument-price-last]")
            val priceText = priceElement?.text()?.replace(",", "")?.trim()

            val price = priceText?.toDoubleOrNull() ?: 0.0

            val type = if (doc.text().contains("서울") || doc.text().contains("KOSPI")) {
                StockType.DOMESTIC
            } else {
                StockType.FOREIGN

            }
            var finalPrice = price
            if(type== StockType.FOREIGN){
                var rate = getUsdToKrwRate()
                finalPrice = price * rate
            }
            return finalPrice


        } catch (e: Exception) {
            e.printStackTrace()
            return 0.0

        }
    }

    suspend fun getUpbitMarketList(): List<Stock> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.upbit.com/v1/market/all?isDetails=false"
            val json = Jsoup.connect(url)
                .ignoreContentType(true)
                .get()
                .body()
                .text()

            val arr = JSONArray(json)
            val list = mutableListOf<Stock>()

            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)

                val symbol = obj.getString("market")      // ex: KRW-BTC
                val name = obj.getString("korean_name")   // ex: 비트코인

                // KRW 마켓만 사용
                if (symbol.startsWith("KRW-")) {
                    list.add(
                        Stock(
                            symbol = symbol,
                            code = symbol,
                            name = name,
                            type = StockType.CRYPTO,
                            price = 0.0
                        )

                    )
                }
            }

            list
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    suspend fun getUpbitCrypto(stock: Stock): Stock = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.upbit.com/v1/ticker?markets=${stock.symbol}"

            val json = Jsoup.connect(url)
                .ignoreContentType(true)
                .get()
                .body()
                .text()

            val arr = JSONArray(json)
            val obj = arr.getJSONObject(0)
            val price = obj.getDouble("trade_price")

            stock.copy(
                price = price,
                type = StockType.CRYPTO
            )
        } catch (e: Exception) {
            e.printStackTrace()
            stock.copy(price = 0.0)
        }
    }
    suspend fun getUpbitCrypto(symbol: String,originPrice: Double): Double {
        try {
            val url = "https://api.upbit.com/v1/ticker?markets=${symbol}"

            val json = Jsoup.connect(url)
                .ignoreContentType(true)
                .get()
                .body()
                .text()

            val arr = JSONArray(json)
            val obj = arr.getJSONObject(0)
            val price = obj.getDouble("trade_price")
            return price

        } catch (e: Exception) {
            e.printStackTrace()
            return 0.0
        }
    }
    suspend fun getUsdToKrwRate(): Double {
        if (rate == null) {
            rate = fetchUsdToKrwRateFromInvesting()
        }
        return rate!!
    }
    suspend fun fetchUsdToKrwRateFromInvesting(): Double = withContext(Dispatchers.IO) {
        val url = "https://kr.investing.com/currencies/usd-krw"
        try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("https://www.google.com/")
                .timeout(20_000)
                .get()

            // ✅ 가격 부분은 아래 구조 중 하나
            // <div data-test="instrument-price-last">1,452.20</div>
            val priceElement = doc.selectFirst("div[data-test=instrument-price-last]")
            val priceText = priceElement?.text()?.replace(",", "")?.trim()
            val rate = priceText?.toDoubleOrNull() ?: 1300.0

            Log.d("jdw", "현재 원달러 환율: $rate")
            rate
        } catch (e: Exception) {
            e.printStackTrace()
            1400.0 // 실패 시 기본값
        }
    }

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