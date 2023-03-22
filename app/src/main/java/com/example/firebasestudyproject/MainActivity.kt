package com.example.firebasestudyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasestudyproject.databinding.ActivityForgotPasswordBinding
import com.example.firebasestudyproject.databinding.ActivityMainBinding
import com.example.firebasestudyproject.databinding.ActivityRegisterBinding

class MainActivity : AppCompatActivity() {
    lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

    }
}