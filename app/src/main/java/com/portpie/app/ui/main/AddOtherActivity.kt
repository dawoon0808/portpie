package com.portpie.app.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.StockType
import com.portpie.app.databinding.ActivityAddOtherBinding
import com.portpie.app.ui.viewmodel.OwnedAssetViewModel

class AddOtherActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddOtherBinding
    private lateinit var ownedAssetViewModel: OwnedAssetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddOtherBinding.inflate(layoutInflater)
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

            var name = binding.etName.text.toString()
            if (amount == 0.0){
                Toast.makeText(this@AddOtherActivity,"0보다 큰 수량을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(name.isEmpty()){
                Toast.makeText(this@AddOtherActivity,"자산이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val ownedAsset = OwnedAsset(
                name = name,
                symbol = "other",
                code = "other",
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