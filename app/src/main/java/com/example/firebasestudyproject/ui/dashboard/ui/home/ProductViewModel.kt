package com.example.firebasestudyproject.ui.dashboard.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Product Fragment"
    }
    val text: LiveData<String> = _text
}