package com.example.firebasestudyproject.utils

import android.content.Context
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.firebasestudyproject.R

class GlideLoader(val context: Context) {

    fun loadImage(imageUri: Uri, imageView: AppCompatImageView) {
        try {
            Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}