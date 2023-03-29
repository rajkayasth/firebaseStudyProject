package com.example.firebasestudyproject.ui.productdetails

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityProductDetailsBinding
import com.example.firebasestudyproject.databinding.ActivityProfileBinding
import com.example.firebasestudyproject.firestore.FireStoreClass
import com.example.firebasestudyproject.model.Product
import com.example.firebasestudyproject.utils.Constants
import com.example.firebasestudyproject.utils.GlideLoader

class ProductDetailsActivity : BaseActivity() {
    lateinit var dataBinding: ActivityProductDetailsBinding

    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("PRODUCT_ID", "onCreate: $mProductId")
        }
        var productOwnerId = ""
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
            Log.i("PRODUCT_ID", "onCreate: $productOwnerId")
        }
        if (FireStoreClass().getCurrentUssrId() == productOwnerId) {
            dataBinding.btnAddToCart.visibility = View.GONE
        }else{
            dataBinding.btnAddToCart.visibility = View.VISIBLE
        }
        getProductDetialsById()

    }

    private fun setupActionBar() {
        setSupportActionBar(dataBinding.toolbarProductDetails)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        dataBinding.toolbarProductDetails.setNavigationOnClickListener { onBackPressed() }
    }

    fun getProductDetialsById() {
        showProgressDialog("")
        FireStoreClass().getProductDetailsById(this@ProductDetailsActivity, productId = mProductId)
    }

    @SuppressLint("SetTextI18n")
    fun productDetailsSuccess(product: Product) {
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadImage(
            product.image,
            dataBinding.imgProductDetails
        )
        dataBinding.apply {
            tvProductDetailsTitle.text = product.title
            tvProductDetailsPice.text = "$" + product.price
            tvProductDescription.text = product.description
            tvProductQuantity.text = product.stock_quantity
        }
    }
}