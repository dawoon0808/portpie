package com.portpie.app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portpie.app.data.local.AppDatabase
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.StockType
import com.portpie.app.data.repository.OwnedAssetRepository
import com.portpie.app.data.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OwnedAssetViewModel(context: Context, private val stockRepository: StockRepository = StockRepository() ) : ViewModel() {
    private val repository = OwnedAssetRepository(AppDatabase.getInstance(context).ownedAssetDao())

    private val _assets = MutableStateFlow<List<OwnedAsset>>(emptyList())

    val asset = _assets.asStateFlow()

    fun loadAssets(){
        viewModelScope.launch {

            _assets.value = repository.getAll()
        }
    }
    private val _assetInsertResult = MutableStateFlow<Boolean?>(null)
    val assetInsertResult = _assetInsertResult.asStateFlow()
    fun addAsset(asset: OwnedAsset) {
        viewModelScope.launch {

            val success = repository.insert(asset)
            _assetInsertResult.value = success
            loadAssets()
        }
    }

    fun deleteAsset(asset: OwnedAsset) {
        viewModelScope.launch {
            repository.delete(asset)
            loadAssets()
        }
    }
    fun updateAssetAmount(id: Int, newAmount: Double) {
        viewModelScope.launch {
            repository.updateAmount(id, newAmount)
            loadAssets()   // 변경 후 리스트 다시 불러오기
        }
    }
    fun updateAssetPrice(id: Int, price: Double) {
        viewModelScope.launch {
            repository.updatePrice(id, price)
            loadAssets()   // 변경 후 리스트 다시 불러오기
        }
    }
    fun refreshPrices() {
        viewModelScope.launch(Dispatchers.IO) {
            val allAssets = repository.getAll()
            for (asset in allAssets) {
                try {
                    // ✅ 자산 종류별로 최신 가격 가져오기
                    if(asset.type == StockType.CRYPTO){
                        var price = stockRepository.getUpbitCrypto(asset.symbol,asset.price)  // 국내/해외 주식

                        repository.updatePrice(asset.id, price)
                    }else if(asset.type == StockType.CASH){

                        repository.updatePrice(asset.id, asset.price)
                    }else if(asset.type== StockType.GOLD){
                        var price = stockRepository.getGoldSpotPriceKR()
                        repository.updatePrice(asset.id,price)
                    }else if(asset.type== StockType.ETF_DOMESTIC){
                        var price = stockRepository.getInvestingKREtf(asset.symbol,asset.price)  // 국내/해외 주식

                        repository.updatePrice(asset.id, price)
                    }else if(asset.type== StockType.ETF_FOREIGN){
                        var price = stockRepository.getInvestingKREtf(asset.symbol,asset.price)  // 국내/해외 주식

                        repository.updatePrice(asset.id, price)
                    }else{
                        var price = stockRepository.getInvestingKRStock(asset.symbol,asset.price)  // 국내/해외 주식

                        repository.updatePrice(asset.id, price)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            loadAssets() // ✅ 전체 새로고침
        }
    }
}