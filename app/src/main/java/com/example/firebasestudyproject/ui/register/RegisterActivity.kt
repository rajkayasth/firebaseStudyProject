package com.example.firebasestudyproject.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityRegisterBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.User
import com.example.firebasestudyproject.ui.login.LoginActivity
import com.example.firebasestudyproject.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : BaseActivity(), View.OnClickListener {
    lateinit var dataBinding: ActivityRegisterBinding
    private var mAuth: FirebaseAuth? = null
    lateinit var mGoogleClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        Utils.hideStatusBar(window)
        initialisedUI()
        mAuth = FirebaseAuth.getInstance()

    }

    companion object {
        private val TAG = "GOOGLE_SIGNIN"
        private val RC_SIGN_IN = 9001
    }

    private fun initialisedUI() {
        dataBinding.listner = this@RegisterActivity
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mAuth = FirebaseAuth.getInstance()
        mGoogleClient = GoogleSignIn.getClient(this@RegisterActivity, gso)
    }

    override fun onClick(v: View?) {
        when (v) {
            dataBinding.imgGoogleRegister -> {
                signInToGoogle()
            }

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

    private fun signInToGoogle() {
        val signInIntent = mGoogleClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.resultCode == RC_SIGN_IN) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        Toast.makeText(this, "GOOGLE SIGN IN SUCCESS", Toast.LENGTH_SHORT).show()
                        firebaseAuthWithGoogle(account!!)

                    } catch (e: Exception) {
                        Log.e(TAG, "Google Sign IN FAILED ", e)
                    }
                }
            }
        }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        //val credentials = GoogleAuthPr
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
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val user = User(
                                id = firebaseUser.uid,
                                firstName = dataBinding.etFirstName.text.toString().trim(),
                                lastName = dataBinding.etLastName.text.toString().trim(),
                                email = dataBinding.etEmailRegister.text.toString().trim(),
                            )


                            FireStoreClass().registerUser(this@RegisterActivity, user)

//                            FirebaseAuth.getInstance().signOut()
//                            finish()
                        } else {
                            hideProgressDialog()
                            task.exception?.message?.let { showErrorSnackBar(it, true) }
                        }
                    })

        }
    }

    fun userRegisterSucesss() {
        hideProgressDialog()
        Toast.makeText(
            this@RegisterActivity,
            "You have registered successfully",
            Toast.LENGTH_SHORT
        ).show()
    }
}