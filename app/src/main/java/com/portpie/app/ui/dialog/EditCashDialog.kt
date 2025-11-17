package com.portpie.app.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.ui.listener.EditAssetDialogListener
import androidx.core.widget.addTextChangedListener
import com.portpie.app.databinding.DialogEditCashBinding

class EditCashDialog (
    private val context: Context,
    private val asset: OwnedAsset,
    private val listener: EditAssetDialogListener
)
{
    fun show(){
        val binding = DialogEditCashBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding.tvName.text = "${asset.name}(${asset.code})"

        binding.etCount.setText(asset.price.toString())
        binding.etCount.addTextChangedListener(){

        }

        binding.btSave.setOnClickListener {
            val newAmount = binding.etCount.text.toString().toDoubleOrNull()?: asset.amount
            if (newAmount == 0.0){
                Toast.makeText(context,"0보다 큰 수량을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
           listener.onSave(asset.type,asset.id,newAmount)
            dialog.dismiss()
        }
        dialog.show()
    }


}