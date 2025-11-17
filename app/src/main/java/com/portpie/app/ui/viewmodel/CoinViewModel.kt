package com.portpie.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portpie.app.data.model.UpbitTicker
import com.portpie.app.data.repository.CoinRepository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoinViewModel : ViewModel(){
    private  val repository = CoinRepository()

    private  val _coinPrices = MutableStateFlow<UpbitTicker?>(null)
    val ticker: StateFlow<UpbitTicker?> = _coinPrices


    init{
        startFetchingPrices()
    }

    private fun startFetchingPrices(){
        viewModelScope.launch {
            while (true){
                try{
                    var result = repository.getPrice("KRW-XRP")
                    if(result!=null){
                        _coinPrices.value = result
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
                delay(10_000L)
            }
        }
    }
}