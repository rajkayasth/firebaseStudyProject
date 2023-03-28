package com.example.firebasestudyproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var userId: String = "",
    var userName: String = "",
    var title: String = "",
    var price:String ="",
    var description: String = "",
    var stock_quantity: String = "",
    var image: String = "",
    var product_id: String = ""
) : Parcelable
