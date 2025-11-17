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
import com.portpie.app.data.model.StockType

import com.portpie.app.databinding.ActivitySearchBinding
import com.portpie.app.ui.viewmodel.StockViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private val viewModel : StockViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.iconBack.setOnClickListener {
            finish()
        }
        val adapter = StockAdapter()
        binding.listStock.adapter = adapter
        binding.listStock.layoutManager = LinearLayoutManager(this)
        binding.btSearch.setOnClickListener {
            val keyword = binding.etSearch.text.toString()
            if(keyword.isNotEmpty()){
                viewModel.search(keyword)
            }

        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredStocks.collectLatest { list->
                    adapter.setStocks(list)
                    Log.d("StockAdapter", "list size: ${list.size}")
                    updateFilterButtons()
                }
            }

        }

        binding.btAll.setOnClickListener {
            viewModel.setFilter(null)
            updateFilterButtons()

        }
        binding.btDomestic.setOnClickListener {
            viewModel.setFilter(StockType.DOMESTIC)
            updateFilterButtons()
        }
        binding.btForeign.setOnClickListener {
            viewModel.setFilter(StockType.FOREIGN)
            updateFilterButtons()
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

    }

    private fun updateFilterButtons() {
        val current = viewModel.getCurrentFilter()
        binding.btAll.isSelected = current == null
        binding.btDomestic.isSelected = current == StockType.DOMESTIC
        binding.btForeign.isSelected = current == StockType.FOREIGN
    }


}