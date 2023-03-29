package com.example.firebasestudyproject.ui.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.FileProvider
import com.example.firebasestudyproject.MainActivity
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityProfileBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.User
import com.example.firebasestudyproject.ui.dashboard.DashBoardActivity
import com.example.firebasestudyproject.utils.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

class ProfileActivity : BaseActivity(), View.OnClickListener {

    lateinit var dataBinding: ActivityProfileBinding
    lateinit var profileImg: AppCompatImageView
    private var profileImageUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mUserProfileImageURL: String = ""

    private val pickFromGalleryContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imgUri ->
            imgUri?.let {
                profileImageUri = imgUri
                GlideLoader(this@ProfileActivity).loadImage(profileImageUri!!, profileImg)
            }
            Log.d("ImageUri", profileImageUri.toString())
        }

    private val cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        GlideLoader(this@ProfileActivity).loadImage(profileImageUri!!, profileImg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        dataBinding.click = this@ProfileActivity
        profileImg = dataBinding.imgUserPhoto
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        dataBinding.apply {

            etFirstNameProfile.setText(mUserDetails.firstName)
            etLastNameProfile.setText(mUserDetails.lastName)
            etEmailProfile.isEnabled = false
            etEmailProfile.setText(mUserDetails.email)

            if (mUserDetails.profileCompleted == 0) {
                txtToolbarTitle.text = getString(R.string.title_profile_completed)
                etFirstNameProfile.isEnabled = false
                etLastNameProfile.isEnabled = false
            } else {
                setupActionBar()
                txtToolbarTitle.text = getString(R.string.title_edit_profile)
                GlideLoader(this@ProfileActivity).loadImage(
                    mUserDetails.image,
                    dataBinding.imgUserPhoto
                )

                if (mUserDetails.mobile.isNotEmpty()) {
                    etMobileProfile.setText(mUserDetails.mobile)
                }
                if (mUserDetails.gender == Constants.MALE) {
                    rbMale.isChecked = true
                } else {
                    rbFeMale.isChecked = true
                }


            }
        }

    }


    private fun setupActionBar() {
        setSupportActionBar(dataBinding.toolBarProfileScreen)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        dataBinding.toolBarProfileScreen.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        when (v) {
            dataBinding.imgAddPhoto -> {

                Dexter.withContext(this@ProfileActivity)
                    .withPermissions(Manifest.permission.CAMERA)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if (report!!.areAllPermissionsGranted()) {


                                CommonDialogs.setConfirmationDialogWithPositiveNaiveButton(
                                    this@ProfileActivity,"",
                                    "Please Select One Option",
                                    object : ConfirmationListener {
                                        override fun onCancelClick() {

                                        }

                                        override fun onYesClick() {
                                            Log.d("TAGGING", "onYesClick: ")
                                            profileImageUri = createImageUri()!!
                                            cameraContracts.launch(profileImageUri)
                                        }

                                        override fun onNoClick() {
                                            Log.d("TAGGING", "onNoClick: ")
                                            pickFromGalleryContract.launch("image/*")
                                        }

                                    }, getString(R.string.lbl_Camera),
                                    getString(R.string.lbl_Gallery)
                                )
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied) {
                                // permission is denied permanently, navigate user to app settings
                                CommonDialogs.showSuccessFailureDialog(
                                    this@ProfileActivity,
                                    false,
                                    resources.getString(R.string.permission_denied_message),
                                    object : SuccessFailureListener {
                                        override fun onOkayClick(success: Boolean) {
                                            val intent = Intent()
                                            intent.action =
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                            val uri = Uri.fromParts(
                                                "package",
                                                packageName, null
                                            )
                                            intent.data = uri
                                            startActivity(intent)
                                        }
                                    })
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()

                        }

                    }).onSameThread()
                    .check()


            }
            dataBinding.btnSaveProfile -> {
                if (validateUserProfile()) {

                    showProgressDialog("")
                    if (profileImageUri != null) {
                        FireStoreClass().uploadImageToCloudStorage(
                            this@ProfileActivity,
                            profileImageUri,
                            Constants.USER_PROFILE_IMAGE
                        )
                    } else {
                        updateUserProfileDetails()
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val firstName = dataBinding.etFirstNameProfile.text.toString().trim()
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRSTNAME] = firstName
        }

        val lastName = dataBinding.etLastNameProfile.text.toString().trim()
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LASTNAME] = lastName
        }

        val mobileNumber =
            dataBinding.etMobileProfile.text.toString().trim { it <= ' ' }

        val gender = if (dataBinding.rbMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }
        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }
        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile) {
            userHashMap[Constants.MOBILE] = mobileNumber
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender

        }
        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.PROFILE_COMPLETED] = 1
        // showProgressDialog("")
        FireStoreClass().updateUserDetails(this@ProfileActivity, userHashMap)
    }

    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.example.firebasestudyproject.fileProvider",
            image
        )
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        showErrorSnackBar("Profile Updated Successfully", false)
        startActivity(Intent(this@ProfileActivity, DashBoardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        //hideProgressDialog()
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    private fun validateUserProfile(): Boolean {
        var isTrue = false
        if (dataBinding.etMobileProfile.text.toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_mobile), true)
            isTrue = false
        }
        if (dataBinding.etMobileProfile.text?.length!! <= 9) {
            showErrorSnackBar(getString(R.string.err_msg_enter_mobile), true)
            isTrue = false
        }
        if (dataBinding.etMobileProfile.text?.length!! >= 10 && dataBinding.etMobileProfile.text.toString()
                .trim().isNotEmpty()
        ) {
            isTrue = true
        }
        return isTrue
    }
}