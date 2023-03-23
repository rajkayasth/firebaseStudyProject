package com.example.firebasestudyproject.customwidgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class CustomRadioButton(context: Context, attr: AttributeSet) :
    AppCompatRadioButton(context, attr) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface = Typeface.createFromAsset(context.assets, "montserrat_medium.ttf")
        typeface = boldTypeface

    }

}