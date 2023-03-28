package com.example.firebasestudyproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.model.Product
import com.example.firebasestudyproject.utils.GlideLoader

open class ProductListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
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
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgHeader = itemView.findViewById<AppCompatImageView>(R.id.imgItem)
        val productTitle = itemView.findViewById<AppCompatTextView>(R.id.txtProductName)
        val productPrice = itemView.findViewById<AppCompatTextView>(R.id.txtProductPrice)


    }
}