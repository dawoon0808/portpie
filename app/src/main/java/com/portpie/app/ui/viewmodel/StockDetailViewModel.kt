package com.portpie.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portpie.app.data.model.Stock
import com.portpie.app.data.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StockDetailViewModel(private val repository: StockRepository = StockRepository()): ViewModel() {
    private val  _stock = MutableStateFlow<Stock?>(null)
    val stock: StateFlow<Stock?> = _stock

    private val _amount = MutableStateFlow(0.0)
    val amount = _amount.asStateFlow()

    fun loadStock(stock: Stock){
        viewModelScope.launch {
            try {
                val result = repository.getInvestingKRStock(stock)

                    _stock.value = result
            }catch (e: Exception){
                e.printStackTrace()
                _stock.value = null
            }
        }
    }
    fun loadCrypto(stock: Stock){
        viewModelScope.launch {
            try {
                val result = repository.getUpbitCrypto(stock)

                _stock.value = result
            }catch (e: Exception){
                e.printStackTrace()
                _stock.value = null
            }
        }
    }
    val totalValue = combine(_stock, _amount) { s, a ->
        if (s != null) s.price * a else 0.0
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    fun setAmount(value: Double) {
        _amount.value = value
    }
}