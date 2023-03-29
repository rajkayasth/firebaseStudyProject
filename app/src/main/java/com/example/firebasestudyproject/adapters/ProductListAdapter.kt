package com.example.firebasestudyproject.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.model.Product
import com.example.firebasestudyproject.ui.dashboard.ui.products.ProductFragment
import com.example.firebasestudyproject.ui.productdetails.ProductDetailsActivity
import com.example.firebasestudyproject.utils.Constants
import com.example.firebasestudyproject.utils.GlideLoader

open class ProductListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>,
    private val fragment: ProductFragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.raw_product_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            GlideLoader(context).loadImage(model.image, holder.imgHeader)
            holder.productTitle.text = model.title
            holder.productPrice.text = "$${model.price}"

            holder.imgDeleteIcon.setOnClickListener {
                //TODO Delete the Product
                fragment.deleteProduct(model.product_id)
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.userId)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgHeader: AppCompatImageView = itemView.findViewById(R.id.imgItem)
        val productTitle: AppCompatTextView = itemView.findViewById(R.id.txtProductName)
        val productPrice: AppCompatTextView = itemView.findViewById(R.id.txtProductPrice)
        val imgDeleteIcon: AppCompatImageView = itemView.findViewById(R.id.imgDeleteProduct)

    }
}