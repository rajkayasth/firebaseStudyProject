package com.example.firebasestudyproject.ui.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.FileProvider
import com.example.firebasestudyproject.MainActivity
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityProfileBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.User
import com.example.firebasestudyproject.utils.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

class ProfileActivity : BaseActivity(), View.OnClickListener {

    lateinit var dataBinding: ActivityProfileBinding
    lateinit var profileImg: AppCompatImageView
    private lateinit var imageUri: Uri
    private lateinit var mUserDetails: User

    private val pickFromGalleryContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imgUri ->
            imgUri?.let {
                GlideLoader(this@ProfileActivity).loadImage(imgUri, profileImg)
            }
            Log.d("ImageUri", imgUri.toString())
        }

    private val cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        GlideLoader(this@ProfileActivity).loadImage(imageUri, profileImg)
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
            etFirstNameProfile.isEnabled = false
            etFirstNameProfile.setText(mUserDetails.firstName)

            etLastNameProfile.isEnabled = false
            etLastNameProfile.setText(mUserDetails.lastName)

            etEmailProfile.isEnabled = false
            etEmailProfile.setText(mUserDetails.email)
        }

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
                                    this@ProfileActivity,
                                    "Please Select One Option",
                                    object : ConfirmationListener {
                                        override fun onCancelClick() {

                                        }

                                        override fun onYesClick() {
                                            Log.d("TAGGING", "onYesClick: ")
                                            imageUri = createImageUri()!!
                                            cameraContracts.launch(imageUri)
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
                    val userHashMap = HashMap<String, Any>()

                    val mobileNumber =
                        dataBinding.etMobileProfile.text.toString().trim { it <= ' ' }

                    val gender = if (dataBinding.rbMale.isChecked) {
                        Constants.MALE
                    } else {
                        Constants.FEMALE
                    }
                    if (mobileNumber.isNotEmpty()) {
                        userHashMap[Constants.MOBILE] = mobileNumber
                    }
                    userHashMap[Constants.GENDER] = gender
                    showProgressDialog("")
                    FireStoreClass().updateUserDetails(this@ProfileActivity, userHashMap)

                }
            }
        }
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
        startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        finish()
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