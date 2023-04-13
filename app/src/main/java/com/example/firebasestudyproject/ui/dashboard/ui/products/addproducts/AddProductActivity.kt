package com.example.firebasestudyproject.ui.dashboard.ui.products.addproducts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.FileProvider
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityAddProductBinding
import com.example.firebasestudyproject.databinding.ActivityDashBoardBinding
import com.example.firebasestudyproject.databinding.FragmentProductsBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.Product
import com.example.firebasestudyproject.utils.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var _binding: ActivityAddProductBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var profileImg: AppCompatImageView
    private var profileImageUri: Uri? = null
    private var mProductImageUrl: String = ""

    private val pickFromGalleryContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imgUri ->
            imgUri?.let {
                profileImageUri = imgUri
                GlideLoader(this@AddProductActivity).loadImage(profileImageUri!!, profileImg)
            }
            Log.d("ImageUri", profileImageUri.toString())
        }

    private val cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        GlideLoader(this@AddProductActivity).loadImage(profileImageUri!!, profileImg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        binding.clickListener = this@AddProductActivity
        profileImg = binding.imgHeaderMain

    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolBarAddProduct)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        binding.toolBarAddProduct.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnProductSubmit -> {
                if (validateAddProductDetails()) {
                    //  showErrorSnackBar("Product are valid", false)
                    uploadProductImage()
                }
            }
            binding.imgAddProduct -> {
                Dexter.withContext(this@AddProductActivity)
                    .withPermissions(Manifest.permission.CAMERA)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if (report!!.areAllPermissionsGranted()) {
                                CommonDialogs.setConfirmationDialogWithPositiveNaiveButton(
                                    this@AddProductActivity,"",
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
                                    this@AddProductActivity,
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
        }
    }

    private fun createImageUri(): Uri? {
        val image =
            File(applicationContext.filesDir, "camera_photo${System.currentTimeMillis()}.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.example.firebasestudyproject.fileProvider",
            image
        )
    }

    private fun validateAddProductDetails(): Boolean {
        return when {
            profileImageUri == null -> {
                showErrorSnackBar(getString(R.string.err_image_select), true)
                false
            }
            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.err_product_title), true)
                false
            }
            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.err_product_price), true)
                false
            }
            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.err_product_desc), true)
                false
            }
            TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(getString(R.string.err_product_quantity), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun uploadProductImage() {
        showProgressDialog("")
        FireStoreClass().uploadImageToCloudStorage(
            this@AddProductActivity,
            profileImageUri,
            Constants.PRODUCT_IMAGE
        )
    }

    fun imageUploadSuccess(imageURL: String) {
//        hideProgressDialog()
        /* mUserProfileImageURL = imageURL
         updateUserProfileDetails()*/
        mProductImageUrl = imageURL
//        showErrorSnackBar("Image uploaded successfully $imageURL", false)

        uploadProductDetails()
    }

    fun productUploadSuccess() {
        hideProgressDialog()
        Toast.makeText(this@AddProductActivity, "Uploaded successfully", Toast.LENGTH_SHORT).show()
        finish()
        profileImageUri = null
    }


    private fun uploadProductDetails() {
        val userName = this.getSharedPreferences(Constants.APP_PREFERENCE, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME, "")!!

        val product = Product(
            userId = FireStoreClass().getCurrentUssrId(),
            userName = userName,
            title = binding.etProductTitle.text.toString().trim(),
            price = binding.etProductPrice.text.toString().trim(),
            description = binding.etProductDescription.text.toString().trim(),
            stock_quantity = binding.etProductQuantity.text.toString().trim(),
            image = mProductImageUrl,
            product_id = ""
        )

        FireStoreClass().uploadProductDetails(this@AddProductActivity, product)
    }


}