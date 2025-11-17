package com.portpie.app.ui.listener

import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.StockType

interface EditAssetDialogListener {
    fun onSave(type: StockType, id: Int, newAmount: Double)
    fun onDelete(asset: OwnedAsset)
    fun onCancel()
}