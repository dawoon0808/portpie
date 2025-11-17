package com.portpie.app.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.portpie.app.R
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.data.model.StockType
import com.portpie.app.databinding.ActivityMainBinding
import com.portpie.app.ui.dialog.AddAssetDialog
import com.portpie.app.ui.dialog.DeleteAssetDialog
import com.portpie.app.ui.dialog.EditAssetDialog
import com.portpie.app.ui.dialog.EditCashDialog
import com.portpie.app.ui.listener.AddAssetDialogListener
import com.portpie.app.ui.listener.EditAssetDialogListener
import com.portpie.app.ui.listener.OnAssetClickListener
import com.portpie.app.ui.viewmodel.CoinViewModel
import com.portpie.app.ui.viewmodel.OwnedAssetViewModel
import com.portpie.app.ui.viewmodel.StockViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: CoinViewModel by viewModels()
    private val stockViewModel: StockViewModel by viewModels()

    private lateinit var assetAdapter: OwnedAssetAdapter
    private lateinit var ownedAssetViewModel: OwnedAssetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ownedAssetViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return OwnedAssetViewModel(applicationContext) as T
                }
            }
        )[OwnedAssetViewModel::class.java]

        binding.tabAll.setOnClickListener {
            selectTab(binding.tabAll)
            replaceFragment(AssetFragment())
        }
        binding.tabDomestic.setOnClickListener {
            selectTab(binding.tabDomestic)
            replaceFragment(DomesticFragment())
        }
        binding.tabCoin.setOnClickListener {
            selectTab(binding.tabCoin)
            replaceFragment(CoinFragment())
        }
        binding.tabForeign.setOnClickListener {
            selectTab(binding.tabForeign)
            replaceFragment(ForeignFragment())
        }
//        observePrices()


        binding.btAdd.setOnClickListener {
            AddAssetDialog(context=this@MainActivity, listener = object : AddAssetDialogListener{
                override fun onClickListener(type: String) {
                    if(type=="CRYPTO"){
                        val intent = Intent(this@MainActivity, SearchCryptoActivity::class.java)
                        startActivity(intent)

                    }else if(type=="STOCK"){
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        startActivity(intent)
                    }else if(type=="CASH"){
                        val intent = Intent(this@MainActivity, AddCashActivity::class.java)
                        startActivity(intent)
                    }else if(type=="OTHER"){
                        val intent = Intent(this@MainActivity, AddOtherActivity::class.java)
                        startActivity(intent)
                    }else if(type=="GOLD"){
                        val intent = Intent(this@MainActivity, GoldActivity::class.java)
                        startActivity(intent)
                    }
                }
            }).show()

        }

        assetAdapter = OwnedAssetAdapter(object : OnAssetClickListener{
            override fun onItemClick(asset: OwnedAsset) {
                if(asset.type== StockType.CASH){
                    EditCashDialog(context = this@MainActivity,
                        asset = asset,
                        listener = object : EditAssetDialogListener{
                            override fun onSave(type: StockType, id: Int, newAmount: Double) {
                                TODO("Not yet implemented")
                                if (type == StockType.CASH) {
                                    ownedAssetViewModel.updateAssetPrice(id, newAmount)
                                } else {
                                    ownedAssetViewModel.updateAssetAmount(id, newAmount)
                                }
                            }

                            override fun onCancel() {
                                TODO("Not yet implemented")
                            }

                            override fun onDelete(asset: OwnedAsset) {
                                TODO("Not yet implemented")
                            }
                        }).show()
                }else {
                    EditAssetDialog(
                        context = this@MainActivity,
                        asset = asset,
                        listener = object : EditAssetDialogListener {
                            override fun onSave(type: StockType, id: Int, newAmount: Double) {
                                if (type == StockType.CASH) {
                                    ownedAssetViewModel.updateAssetPrice(id, newAmount)
                                } else {
                                    ownedAssetViewModel.updateAssetAmount(id, newAmount)
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
                DeleteAssetDialog(context = this@MainActivity,asset = asset, listener = object :
                    EditAssetDialogListener{
                    override fun onDelete(asset: OwnedAsset) {
                        ownedAssetViewModel.deleteAsset(asset)
                    }

                    override fun onSave(type: StockType,id:Int,newAmount: Double) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancel() {

                    }
                }).show()
            }
        })
        selectTab(binding.tabAll)
        replaceFragment(AssetFragment())
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit()
    }
    override fun onResume() {
        super.onResume()
        ownedAssetViewModel.loadAssets()
        ownedAssetViewModel.refreshPrices()
    }

    private fun selectTab(tab: TextView) {
        binding.tabAll.isSelected = false
        binding.tabDomestic.isSelected = false
        binding.tabForeign.isSelected = false
        binding.tabCoin.isSelected = false

        tab.isSelected = true
    }

}