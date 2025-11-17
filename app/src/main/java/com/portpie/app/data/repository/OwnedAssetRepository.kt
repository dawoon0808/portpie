package com.portpie.app.data.repository

import com.portpie.app.data.local.OwnedAssetDao
import com.portpie.app.data.model.OwnedAsset

class OwnedAssetRepository(private val dao: OwnedAssetDao) {

    suspend fun getAll() = dao.getAllAssets()
    suspend fun insert(asset: OwnedAsset): Boolean {
        val existing = dao.getAssetBySymbol(asset.symbol)
        return if (existing == null) {
            dao.insertAsset(asset)
            true
        } else {
            false
        }
    }
    suspend fun delete(asset: OwnedAsset) = dao.deleteAsset(asset)
    suspend fun updateAmount(id: Int, amount: Double) = dao.updateAmount(id, amount)
    suspend fun updatePrice(id: Int, price: Double) = dao.updatePrice(id, price)



}