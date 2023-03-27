package com.example.firebasestudyproject.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivitySettingsBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.User
import com.example.firebasestudyproject.ui.login.LoginActivity
import com.example.firebasestudyproject.ui.profile.ProfileActivity
import com.example.firebasestudyproject.utils.Constants
import com.example.firebasestudyproject.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var databinding: ActivitySettingsBinding
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(databinding.root)
        setupActionBar()
        databinding.clickListener = this@SettingsActivity

    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(databinding.toolbarSettingsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        databinding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails() {
        showProgressDialog("")
        FireStoreClass().getUserDetials(this@SettingsActivity)
    }

    @SuppressLint("SetTextI18n")
    fun userDetailSuccess(user: User?) {
        if (user != null) {
            mUserDetails = user
        }
        hideProgressDialog()
        user?.let {
            GlideLoader(this@SettingsActivity).loadImage(user.image, databinding.ivUserPhoto)
            databinding.tvName.text = "${user.firstName} ${user.lastName}"
            databinding.tvEmail.text = user.email
            databinding.tvGender.text = user.gender
            databinding.tvMobileNumber.text = user.mobile
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            databinding.tvEdit -> {
                val intent = Intent(this@SettingsActivity, ProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                startActivity(intent)
            }
            databinding.btnLogout -> {
                showErrorSnackBar("click", false)
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}