package com.example.firebasestudyproject.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.firebasestudyproject.MainActivity
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.ui.login.LoginActivity
import com.example.firebasestudyproject.utils.Utils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Utils.hideStatusBar(window)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                /**Launching Main Activity*/
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish() //call this when your activity is done and should be close
            }, 5000
        )
    }
}