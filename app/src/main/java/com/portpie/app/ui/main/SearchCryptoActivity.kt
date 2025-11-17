package com.portpie.app.ui.main

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.portpie.app.databinding.ActivitySearchCryptoBinding
import com.portpie.app.ui.viewmodel.StockViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class SearchCryptoActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySearchCryptoBinding
    private val viewModel : StockViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchCryptoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.iconBack.setOnClickListener {
            finish()
        }
        val adapter = CryptoAdapter()
        binding.listStock.adapter = adapter
        binding.listStock.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredStocks.collectLatest { list->
                    adapter.setStocks(list)
                    Log.d("StockAdapter", "list size: ${list.size}")

                }
            }

        }
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val keyword = binding.etSearch.text.toString()
                if(keyword.isNotEmpty()){
                    viewModel.search(keyword)
                }
                true
            } else {
                false
            }
        }
        binding.btSearch.setOnClickListener {
            val keyword = binding.etSearch.text.toString()
            if(keyword.isNotEmpty()) {
                viewModel.searchCrypto(keyword)
            }
        }

    }


}