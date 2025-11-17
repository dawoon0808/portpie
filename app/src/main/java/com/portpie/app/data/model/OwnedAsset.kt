package com.portpie.app.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "owned_assets",
    indices = [Index(value = ["symbol"], unique = true)]
)
data class OwnedAsset (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,           // 자산 이름 (삼성전자, 비트코인 등)
    val symbol: String,
    val code: String,// 티커 코드 (005930, BTC 등)
    val price: Double,          // 현재가
    val amount: Double,         // 보유 수량
    val totalValue: Double,     // 총 평가 금액
    val type: StockType,           // DOMESTIC, FOREIGN, CRYPTO 등
    val currency: String,       // KRW, USD 등

)