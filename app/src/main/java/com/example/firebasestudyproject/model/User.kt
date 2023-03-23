package com.example.firebasestudyproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class User(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var image: String = "",
    var mobile: String = "",
    var gender: String = "",
    var profileCompleted: Int = 0,
) : Parcelable