package com.portpie.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class StockType{
    DOMESTIC,
    FOREIGN,
    CRYPTO,
    GOLD,
    OTHER,
    CASH,


}
@Parcelize
data class Stock (
    val symbol : String,
    val code : String,
    val name : String,
    val type: StockType,
    val price : Double
): Parcelable