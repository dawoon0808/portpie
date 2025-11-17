package com.portpie.app.ui.listener

import com.portpie.app.data.model.OwnedAsset

interface OnAssetClickListener {
    fun onItemClick(asset: OwnedAsset)
    fun onItemDelete(asset: OwnedAsset)
}