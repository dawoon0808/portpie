package com.portpie.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.portpie.app.data.model.OwnedAsset


@Dao
interface OwnedAssetDao {

    @Query("SELECT * FROM owned_assets")
    suspend fun getAllAssets() : List<OwnedAsset>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAsset(asset: OwnedAsset)

    @Delete
    suspend fun deleteAsset(asset: OwnedAsset)

    @Query("UPDATE owned_assets SET amount = :amount WHERE id = :id")
    suspend fun updateAmount(id: Int, amount: Double)
    @Query("UPDATE owned_assets SET price = :price, totalValue = :price * amount WHERE id = :id")
    suspend fun updatePrice(id: Int, price: Double)

    @Query("SELECT * FROM owned_assets WHERE symbol = :symbol LIMIT 1")
    suspend fun getAssetBySymbol(symbol: String): OwnedAsset?
}