package com.portpie.app.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.StockType
import com.portpie.app.databinding.FragmentForeignBinding
import com.portpie.app.ui.dialog.DeleteAssetDialog
import com.portpie.app.ui.dialog.EditAssetDialog
import com.portpie.app.ui.dialog.EditCashDialog
import com.portpie.app.ui.listener.EditAssetDialogListener
import com.portpie.app.ui.listener.OnAssetClickListener
import com.portpie.app.ui.viewmodel.OwnedAssetViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import kotlin.getValue

class ForeignFragment : Fragment() {
    private var _binding: FragmentForeignBinding? =null
    private val binding get() = _binding!!
    private lateinit var assetAdapter: OwnedAssetAdapter
    private val viewModel: OwnedAssetViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForeignBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listAssets.layoutManager = LinearLayoutManager(requireContext())
        assetAdapter = OwnedAssetAdapter(object : OnAssetClickListener {
            override fun onItemClick(asset: OwnedAsset) {
                if (asset.type == StockType.CASH) {
                    EditCashDialog(
                        context = requireContext(),
                        asset = asset,
                        listener = object : EditAssetDialogListener {
                            override fun onSave(type: StockType, id: Int, newAmount: Double) {

                                if (type == StockType.CASH) {
                                    viewModel.updateAssetPrice(id, newAmount)
                                } else {
                                    viewModel.updateAssetAmount(id, newAmount)
                                }
                            }

                            override fun onCancel() {

                            }

                            override fun onDelete(asset: OwnedAsset) {

                            }
                        }).show()
                } else {
                    EditAssetDialog(
                        context = requireContext(),
                        asset = asset,
                        listener = object : EditAssetDialogListener {
                            override fun onSave(type: StockType, id: Int, newAmount: Double) {
                                if (type == StockType.CASH) {
                                    viewModel.updateAssetPrice(id, newAmount)
                                } else {
                                    viewModel.updateAssetAmount(id, newAmount)
                                }


                            }

                            override fun onCancel() {

                            }

                            override fun onDelete(asset: OwnedAsset) {

                            }
                        }
                    ).show()
                }
            }

            override fun onItemDelete(asset: OwnedAsset) {
                DeleteAssetDialog(context = requireContext(),asset = asset, listener = object :
                    EditAssetDialogListener{
                    override fun onDelete(asset: OwnedAsset) {
                        viewModel.deleteAsset(asset)
                    }

                    override fun onSave(type: StockType,id: Int, newAmount: Double) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancel() {

                    }
                }).show()
            }
        })
        binding.listAssets.adapter = assetAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.asset.collect {assetList->
                updateChart(assetList)

            }
        }
    }

    private fun updateChart(assetList: List<OwnedAsset>) {

        val foreign = assetList
            .filter { it.type == StockType.FOREIGN ||it.type== StockType.ETF_FOREIGN }

        val entries = ArrayList<PieEntry>()

        // 🔥 PieChart 항목 3개
        foreign.forEach { asset->
            val totalValue = asset.amount*asset.price
            if(totalValue>0){
                entries.add(PieEntry(totalValue.toFloat(),asset.name))
            }
        }

        // 🔥 DataSet 구성


        if (entries.isEmpty()) {
            val entries = listOf(PieEntry(1f, "No Data"))
            val dataSet = PieDataSet(entries, "").apply {
                color = Color.LTGRAY
                setDrawValues(false)
            }
            binding.pieChart.data = PieData(dataSet)
            binding.pieChart.invalidate()
        } else {
            val dataSet = PieDataSet(entries, "").apply {
                valueTextSize = 20f
                valueTextColor = Color.WHITE
                valueFormatter = PercentFormatter(binding.pieChart)
                colors = listOf(
                    Color.parseColor("#3ECF8E"),// green
                    Color.parseColor("#FF9F43"), // orange
                    Color.parseColor("#F6C744"), // yellow
                    Color.parseColor("#795548"), // brown

                    Color.parseColor("#2196F3"), // blue
                    Color.parseColor("#9C27B0"), // purple
                    Color.parseColor("#03A9F4")  // sky
                )
            }
            binding.pieChart.data = PieData(dataSet)
            binding.pieChart.invalidate()
        }
        binding.pieChart.apply {
            this.data = data
            description.isEnabled = false
            legend.isEnabled = false

            setUsePercentValues(true)

            isDrawHoleEnabled = true
            holeRadius = 45f
            transparentCircleRadius = 50f

            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(12f)

            // 애니메이션
            animateY(800)
            invalidate()
        }

        // 총 자산 표시 (원화)
        val total =  foreign.sumOf { it.price*it.amount }
        binding.tvTotalPrice.text = "₩%,.0f".format(total)
        assetAdapter.setStocks(foreign)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}