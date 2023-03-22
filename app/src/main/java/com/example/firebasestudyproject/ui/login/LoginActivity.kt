package com.example.firebasestudyproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityLoginBinding
import com.example.firebasestudyproject.ui.register.RegisterActivity
import com.example.firebasestudyproject.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {

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
                logInRegisterUser()
            }
            binding.txtRegister -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            binding.txtForgotPassword -> {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
        }
    }


    /*endregion*/


    private fun validateLoginDetails(): Boolean {
        return when {

            TextUtils.isEmpty(binding.etEmailLogin.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(
                binding.etPasswordLogin.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                true
            }
        }
    }

    private fun logInRegisterUser() {
        if (validateLoginDetails()) {
            showProgressDialog("")
            //get the text from edit text and password
            val email = binding.etEmailLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        showErrorSnackBar("Login SuccessFully", false)
                    } else {
                        showErrorSnackBar(task.exception?.message.toString(), true)

                    }
                }
        }
    }

}