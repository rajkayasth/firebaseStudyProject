package com.example.firebasestudyproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val userId: String = "",
    val userName: String = "",
    val title: String = "",
    val price:String ="",
    val description: String = "",
    val stock_quantity: String = "",
    val image: String = "",
    val id: String = ""
) : Parcelable
