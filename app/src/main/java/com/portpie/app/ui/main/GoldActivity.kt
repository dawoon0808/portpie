package com.portpie.app.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.StockType
import com.portpie.app.databinding.ActivityGoldBinding
import com.portpie.app.ui.viewmodel.GoldViewModel
import com.portpie.app.ui.viewmodel.OwnedAssetViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class GoldActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoldBinding
    private val viewModel: GoldViewModel by viewModels()
    private lateinit var ownedAssetViewModel: OwnedAssetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoldBinding.inflate(layoutInflater)
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


        val name = "금1kg"
        val code ="gold"
        val type = StockType.GOLD


        binding.tvName.text = name+"($code)"



        var  _price = 0.0

        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val amount = s?.toString()?.toIntOrNull() ?: 0

                binding.tvValueTotal.text = "${amount*_price}"
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        lifecycleScope.launch {
            repeatOnLifecycle (Lifecycle.State.STARTED){
                viewModel.goldPrice.collectLatest{price->
                    if (price > 0) {
                        _price = price
                        binding.tvPrice.text = "₩%,.0f".format(price)
                    } else {
                        binding.tvPrice.text = "조회 실패"
                        _price = price
                    }
                }
            }
        }

        viewModel.loadGoldPrice()
        lifecycleScope.launch {
            ownedAssetViewModel.assetInsertResult.collectLatest { success ->
                success?.let {
                    if (it) {
                        Toast.makeText(
                            this@GoldActivity,
                            "자산이 추가되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@GoldActivity,
                            "이미 존재하는 종목입니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btSave.setOnClickListener {

            val amount = binding.etCount.text.toString().toDoubleOrNull() ?: 0.0
            val total = _price * amount

            if (amount == 0.0){
                Toast.makeText(this@GoldActivity,"0보다 큰 수량을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val ownedAsset = OwnedAsset(
                name = "금",
                symbol = "gold",
                code = "gold",
                price = _price,
                amount = amount,
                totalValue = total,
                type = StockType.GOLD,
                currency = "KRW",

                )
            ownedAssetViewModel.addAsset(ownedAsset)



        }

    }


}