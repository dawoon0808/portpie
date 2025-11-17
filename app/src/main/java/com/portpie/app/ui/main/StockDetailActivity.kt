package com.portpie.app.ui.main

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.Stock
import com.portpie.app.data.model.StockType
import com.portpie.app.databinding.ActivityStockDetailBinding
import com.portpie.app.ui.viewmodel.OwnedAssetViewModel
import com.portpie.app.ui.viewmodel.StockDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockDetailBinding
    private val viewModel: StockDetailViewModel by viewModels()
    private lateinit var ownedAssetViewModel: OwnedAssetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(layoutInflater)
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
        val stock: Stock? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("stock", Stock::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("stock")
        }
        if (stock == null) {
            finish() // 데이터 없으면 화면 종료
            return
        }
        val name = stock?.name
        val code =stock?.code
        val type = stock?.type


        binding.tvName.text = name+"($code)"

        if(type== StockType.CRYPTO){
            viewModel.loadCrypto(stock)
        }else {
            viewModel.loadStock(stock)
        }
        lifecycleScope.launch {
            repeatOnLifecycle (Lifecycle.State.STARTED){
                viewModel.stock.collectLatest {stock->
                    stock?.let{
                        binding.tvPrice.text = if (it.price % 1 == 0.0) {
                            "₩%,.0f".format(it.price)      // 정수 → 소수점 없음
                        } else {
                            "₩%,.2f".format(it.price)      // 소수 → 소수점 2자리 표시
                        }

                    }
                }
            }
        }
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val amount = s?.toString()?.toDoubleOrNull() ?: 0.0
                viewModel.setAmount(amount)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        lifecycleScope.launch {
            viewModel.totalValue.collectLatest { total ->
                binding.tvValueTotal.text = "₩%,.0f".format(total)
            }
        }
        lifecycleScope.launch {
            ownedAssetViewModel.assetInsertResult.collectLatest { success ->
                success?.let {
                    if (it) {
                        Toast.makeText(
                            this@StockDetailActivity,
                            "자산이 추가되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@StockDetailActivity,
                            "이미 존재하는 종목입니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.etCount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // TODO: 확인 눌렀을 때 실행할 코드
                save()
                true
            } else {
                false
            }
        }
        binding.btSave.setOnClickListener {
            save()



        }

    }
    fun save(){
        val stock = viewModel.stock.value ?: return
        val amount = binding.etCount.text.toString().toDoubleOrNull() ?: 0.0
        val total = stock.price * amount

        if (amount == 0.0){
            Toast.makeText(this@StockDetailActivity,"0보다 큰 수량을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val ownedAsset = OwnedAsset(
            name = stock.name,
            symbol = stock.symbol,
            code = stock.code,
            price = stock.price,
            amount = amount,
            totalValue = total,
            type = stock.type,
            currency = "KRW",

            )
        ownedAssetViewModel.addAsset(ownedAsset)



    }

}