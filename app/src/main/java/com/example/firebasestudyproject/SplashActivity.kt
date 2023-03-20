package com.example.firebasestudyproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.example.firebasestudyproject.utils.Utils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Utils.hideStatusBar(window)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                /**Launching Main Activity*/
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish() //call this when your activity is done and should be close
            }, 5000
        )
    }
}