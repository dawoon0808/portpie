package com.portpie.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portpie.app.data.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoldViewModel() : ViewModel() {

    private val repository = StockRepository()

    private val _goldPrice = MutableStateFlow(0.0)
    val goldPrice: StateFlow<Double> = _goldPrice
    fun loadGoldPrice() {
        viewModelScope.launch {
            val price = repository.getGoldSpotPriceKR()
            _goldPrice.value = price
        }
    }
}