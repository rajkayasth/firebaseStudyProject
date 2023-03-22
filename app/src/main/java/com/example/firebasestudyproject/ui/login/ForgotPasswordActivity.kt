package com.example.firebasestudyproject.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {
    lateinit var dataBinding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        initializedUI()
    }

    private fun initializedUI() {
        dataBinding.clickListner = this@ForgotPasswordActivity
    }

    override fun onClick(v: View?) {
        when (v) {
            dataBinding.btnSubmitForgotPassword -> {
                val email = dataBinding.etEmailLogin.text.toString().trim()
                if (email.isEmpty()) {
                    showErrorSnackBar(getString(R.string.err_msg_enter_email), true)
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showErrorSnackBar("email Send Successfully", false)
                                finish()
                            } else {
                                showErrorSnackBar(task.exception?.message.toString(), true)
                            }
                        }
                }
            }
        }
    }
}