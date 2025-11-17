package com.portpie.app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.databinding.ItemAssetBinding
import com.portpie.app.ui.listener.OnAssetClickListener

class OwnedAssetAdapter(
   private val listener: OnAssetClickListener
) : RecyclerView.Adapter<OwnedAssetAdapter.OwnedAssetHolder>() {
    private val items = mutableListOf<OwnedAsset>()

    inner class OwnedAssetHolder(val binding: ItemAssetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val df = java.text.DecimalFormat("0.##########")
        fun bind(asset: OwnedAsset) {
            binding.tvName.text = "${asset.name}"
            binding.tvCount.text = "수량 : ${df.format(asset.amount)}"
            binding.tvPrice.text = "%,.0f원".format(asset.price*asset.amount)
            binding.iconDelte.setOnClickListener {
                listener.onItemDelete(asset)
            }
            binding.iconEdit.setOnClickListener {
                listener.onItemClick(asset)
            }
//            binding.tvPrice.text = stock.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnedAssetHolder {
        val binding = ItemAssetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OwnedAssetHolder(binding)
    }

    override fun onBindViewHolder(holder: OwnedAssetHolder, position: Int) {
        holder.bind(items[position])
    }
    fun setStocks(newList: List<OwnedAsset>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged() // 전체 갱신
    }
    override fun getItemCount(): Int = items.size

}