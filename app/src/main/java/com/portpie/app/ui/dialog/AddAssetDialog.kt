package com.portpie.app.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.portpie.app.databinding.DialogAddAssetBinding
import com.portpie.app.ui.listener.AddAssetDialogListener

class AddAssetDialog (
    private val context: Context,
    private val listener: AddAssetDialogListener
)
{
    fun show(){
        val binding = DialogAddAssetBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)

            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding.btCrypto.setOnClickListener {
            listener.onClickListener("CRYPTO")
            dialog.dismiss()
        }
        binding.btStock.setOnClickListener {
            listener.onClickListener("STOCK")
            dialog.dismiss()
        }
        binding.btCash.setOnClickListener {
            listener.onClickListener("CASH")
            dialog.dismiss()
        }
        binding.btGold.setOnClickListener {
            listener.onClickListener("GOLD")
            dialog.dismiss()
        }
        dialog.show()
    }


}