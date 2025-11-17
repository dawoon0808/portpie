package com.portpie.app.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.StockType
import com.portpie.app.databinding.ActivityAddCashBinding
import com.portpie.app.ui.viewmodel.OwnedAssetViewModel

class AddCashActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddCashBinding
    private lateinit var ownedAssetViewModel: OwnedAssetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.iconBack.setOnClickListener {
            finish()
        }
        ownedAssetViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return OwnedAssetViewModel(applicationContext) as T
                }
            }
        )[OwnedAssetViewModel::class.java]

        binding.btSave.setOnClickListener {

            val amount = binding.etCount.text.toString().toDoubleOrNull() ?: 0.0

            if (amount == 0.0){
                Toast.makeText(this@AddCashActivity,"0보다 큰 수량을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val ownedAsset = OwnedAsset(
                name = "현금",
                symbol = "cash",
                code = "cash",
                price = amount,
                amount = 1.0,
                totalValue = amount,
                type = StockType.CASH,
                currency = "KRW",

                )
            ownedAssetViewModel.addAsset(ownedAsset)
            finish()


        }

    }

}