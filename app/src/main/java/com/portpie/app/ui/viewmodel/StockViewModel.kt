package com.portpie.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portpie.app.data.model.Stock
import com.portpie.app.data.model.StockType
import com.portpie.app.data.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StockViewModel(private val repository: StockRepository = StockRepository()): ViewModel() {
    private val _stock = MutableStateFlow<Stock?>(null)
    val stock: StateFlow<Stock?> = _stock


    init {
//        startFetching()
    }
//    fun startFetching(){
//        viewModelScope.launch {
//            while (true){
//                try {
//                    val newStrock = repository.getSamsungStock()
//                    _stock.value = newStrock
//
//                }catch (e: Exception){
//                    e.printStackTrace()
//                }
//                delay(10_000L)
//            }
//        }
//    }
    private val _searchResults = MutableStateFlow<List<Stock>>(emptyList())
    val searchResult : StateFlow<List<Stock>> = _searchResults
    private val _filterType = MutableStateFlow<StockType?>(null)
    val filteredStocks: StateFlow<List<Stock>> = combine(_searchResults, _filterType) { stocks, filter ->
        val filtered = filter?.let { f -> stocks.filter { it.type == f } } ?: stocks
        filtered.toList() // 항상 새로운 리스트 객체 생성
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun setStocks(stocks: List<Stock>) {
        _searchResults.value = stocks
    }

    // 필터 변경
    fun setFilter(type: StockType?) {
        _filterType.value = type
    }
    fun getCurrentFilter(): StockType? = _filterType.value
    fun search(keyword : String){
        viewModelScope.launch {
            try {
                val results = repository.searchStockFromInvesting(keyword)
                setStocks(results)
            }catch (e: Exception){
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }
    suspend fun searchUpbit(keyword: String): List<Stock> {
        val markets = repository.getUpbitMarketList()
        return markets.filter {
            it.name.contains(keyword) || it.symbol.contains(keyword, ignoreCase = true)
        }
    }
    fun searchCrypto(keyword: String) {
        viewModelScope.launch {
            val results = searchUpbit(keyword)
            _searchResults.value = results
        }
    }

}