package com.example.firebasestudyproject.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.databinding.DialogShowConfirmationPositiveNagetiveButtonBinding
import com.example.firebasestudyproject.databinding.DialogSuccessFailureBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object CommonDialogs {

    fun setConfirmationDialogWithPositiveNaiveButton(
        mContext: Context,
        title: String,
        text: String?,
        listener: ConfirmationListener?,
        btnYesName: String,
        btnNoName: String
    ) {
        val dialog = Dialog(mContext, R.style.WideDialog)
        val binding =
            DialogShowConfirmationPositiveNagetiveButtonBinding.inflate(LayoutInflater.from(mContext))
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        binding.info = text
        binding.title = title
        binding.txtConfirmationMessage.text = text
        binding.btnYesDialog.text = btnYesName
        binding.btnNoDialog.text = btnNoName
        binding.btnYesDialog.setOnClickListener {
            dialog.dismiss()
            listener?.onYesClick()

        }
        binding.btnNoDialog.setOnClickListener {
            dialog.dismiss()
            listener?.onNoClick()

        }
        binding.imgClose.setOnClickListener {
            dialog.dismiss()
            listener?.onCancelClick()

        }
        dialog.show()
    }

    fun showSuccessFailureDialog(
        mContext: Context,
        success: Boolean,
        text: String?,
        listener: SuccessFailureListener?
    ) {
        val dialog = Dialog(mContext, R.style.WideDialog)
        val binding = DialogSuccessFailureBinding.inflate(LayoutInflater.from(mContext))
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        binding.text = text
        binding.success = success
        binding.rbAddOrUpdate.setOnClickListener {
            dialog.dismiss()
            listener?.onOkayClick(success)
        }
        dialog.show()
    }


/*    fun showAlertDialog(
        context: Context,
        title: String?,
        description: String?,
        positiveButtonText: String = context.getString(R.string.key_yes),
        negativeButtonText: String = context.getString(R.string.key_no),
        listener: ConfirmationListener?
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(description)
            .setCancelable(false)
            .setPositiveButton(
                positiveButtonText
            ) { dialog, i ->
                dialog.dismiss()
                listener?.onYesClick()
            }
            .setNegativeButton(
                negativeButtonText
            ) { dialog, i ->
                dialog.dismiss()
                listener?.onNoClick()

            }
            .show()
    }
    fun showOkAlertDialog(
        context: Context,
        description: String?,
        positiveButtonText: String = context.getString(R.string.key_ok),
        listener: ConfirmationListener?
    ) {
        MaterialAlertDialogBuilder(context)
            .setMessage(description)
            .setCancelable(false)
            .setPositiveButton(
                positiveButtonText
            ) { dialog, i ->
                dialog.dismiss()
                listener?.onYesClick()
            }
            .show()
    }*/

}