package com.portpie.app.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.portpie.app.data.model.Stock
import com.portpie.app.databinding.ItemStockBinding

class CryptoAdapter() : RecyclerView.Adapter<CryptoAdapter.StockViewHolder>() {
    private val items = mutableListOf<Stock>()

    inner class StockViewHolder(val binding: ItemStockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: Stock) {
            binding.tvName.text = "${stock.name} (${stock.code})"

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, StockDetailActivity::class.java)

                intent.putExtra("stock",stock)

                context.startActivity(intent)

            }
//            binding.tvPrice.text = stock.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(items[position])
    }
    fun setStocks(newList: List<Stock>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged() // 전체 갱신
    }
    override fun getItemCount(): Int = items.size

}