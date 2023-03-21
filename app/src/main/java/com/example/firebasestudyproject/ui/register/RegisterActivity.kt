package com.example.firebasestudyproject.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityRegisterBinding
import com.example.firebasestudyproject.ui.login.LoginActivity
import com.example.firebasestudyproject.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity(), View.OnClickListener {
    lateinit var dataBinding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        Utils.hideStatusBar(window)
        initialisedUI()

    }

    private fun initialisedUI() {
        dataBinding.listner = this@RegisterActivity
    }

    override fun onClick(v: View?) {
        when (v) {
            dataBinding.cbTermsCondtition -> {

            }
            dataBinding.btnRegister -> {
                registerUser()
            }
            dataBinding.txtLogin -> {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
            dataBinding.btnBackRegister -> {
                onBackPressed()
            }
        }
    }

    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(dataBinding.etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(dataBinding.etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(dataBinding.etEmailRegister.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(
                dataBinding.etPasswordRegister.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(
                dataBinding.etConfPasswordRegister.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            dataBinding.etPasswordRegister.text.toString()
                .trim { it <= ' ' } != dataBinding.etConfPasswordRegister.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }
            !dataBinding.cbTermsCondtition.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                false
            }
            else -> {
                //showErrorSnackBar("Register Successful", false)
                true
            }
        }
    }
    // END

    private fun registerUser() {
        //check with validation function if entries are correct or not
        if (validateRegisterDetails()) {

            showProgressDialog("")
            val email: String = dataBinding.etEmailRegister.text.toString().trim()
            val password: String =
                dataBinding.etPasswordRegister.text.toString().trim()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener { task ->
                        hideProgressDialog()
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar("You are Registered Successfully", false)
                        } else {
                            task.exception?.message?.let { showErrorSnackBar(it, true) }
                        }
                    })

        }
    }
}