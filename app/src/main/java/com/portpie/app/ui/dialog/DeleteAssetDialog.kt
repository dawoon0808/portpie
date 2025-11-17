package com.portpie.app.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.portpie.app.data.model.OwnedAsset
import com.portpie.app.ui.listener.EditAssetDialogListener
import com.portpie.app.databinding.DialogDeleteAssetBinding

class DeleteAssetDialog (
    private val context: Context,
    private val asset: OwnedAsset,
    private val listener: EditAssetDialogListener
)
{
    fun show(){
        val binding = DialogDeleteAssetBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding.tvName.text = "${asset.name}(${asset.code})"


        binding.btCancel.setOnClickListener { dialog.dismiss() }
        binding.btDelete.setOnClickListener {
            listener.onDelete(asset)
            dialog.dismiss()
        }
        dialog.show()
    }


}