package com.example.firebasestudyproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasestudyproject.databinding.ActivityMainBinding
import com.example.firebasestudyproject.utils.Constants
import java.util.prefs.Preferences


class MainActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        preferences = getSharedPreferences(Constants.APP_PREFERENCE, Context.MODE_PRIVATE)
        initialisedUI()

    }

    @SuppressLint("SetTextI18n")
    private fun initialisedUI() {

        val userName = preferences.getString(Constants.LOGGED_IN_USERNAME, "")
       // dataBinding.txtUserName.text = "the Logged in UserName $userName"

    }
}