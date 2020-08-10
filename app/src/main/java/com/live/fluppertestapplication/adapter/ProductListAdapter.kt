package com.live.fluppertestapplication.adapter

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.live.fluppertestapplication.R
import com.live.fluppertestapplication.activity.ProductDetailActivity
import com.live.fluppertestapplication.adapter.ProductListAdapter.ProductViewHolder
import com.live.fluppertestapplication.databinding.ItemProductLayoutBinding
import com.live.fluppertestapplication.db.Product
import com.live.fluppertestapplication.utils.AppConstants
import com.live.fluppertestapplication.utils.ConvertorClass
import java.util.*
import kotlin.collections.ArrayList

//, var productList: List<Product>,
class ProductListAdapter(
    var context: Context,
    val itemClick: OnItemClick
) :
    RecyclerView.Adapter<ProductViewHolder>() {
     lateinit var productList: ArrayList<Product>

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductViewHolder {
        val itemProductLayoutBinding: ItemProductLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.item_product_layout, viewGroup, false
            )
        return ProductViewHolder(itemProductLayoutBinding)
    }

    override fun onBindViewHolder(productViewHolder: ProductViewHolder, i: Int) {
        if (productList != null) {
            var productData :Product= productList!![i]
            productViewHolder.itemProductLayoutBinding.productsData=productData
            Glide.with(context!!)
                .load(productData.product_image)
                .placeholder(R.drawable.placeholder)
                .into( productViewHolder.itemProductLayoutBinding.productImage)

        }
        productViewHolder.itemProductLayoutBinding.btnUpdate.setOnClickListener{
            itemClick.onSelectItemClick(productList!![i],true)
        }
        productViewHolder.itemProductLayoutBinding.btnDelete.setOnClickListener{
            itemClick.onSelectItemClick(productList!![i],false)
        }
        productViewHolder.itemView.setOnClickListener{
            context.startActivity(Intent(context,ProductDetailActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(AppConstants.PRODUCT_DATA, ConvertorClass.fromObjectToString(productList!![i])))
        }
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun setList(it: ArrayList<Product>) {
        if(it!=null&&it.size>0){
            productList=it
            notifyDataSetChanged()
        }

    }

    inner class ProductViewHolder( var itemProductLayoutBinding: ItemProductLayoutBinding) :
        RecyclerView.ViewHolder(itemProductLayoutBinding.root)

    interface OnItemClick {
        fun onSelectItemClick(
            product:Product,
            isEdit:Boolean
        )
    }

}