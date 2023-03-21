package com.example.firebasestudyproject.customwidgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface = Typeface.createFromAsset(context.assets, "montserrat.ttf")
        typeface = boldTypeface

    }

}