package com.example.firebasestudyproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.firebasestudyproject.databinding.ActivityLoginBinding
import com.example.firebasestudyproject.ui.register.RegisterActivity
import com.example.firebasestudyproject.utils.Utils

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.hideStatusBar(window)
        initializedUI()

    }

    private fun initializedUI() {
        binding.clickListner = this@LoginActivity
    }

    /*region ONCLICK*/
    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> {

            }
            binding.txtRegister -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
    /*endregion*/
}