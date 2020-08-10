package com.live.fluppertestapplication.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.live.fluppertestapplication.R
import com.live.fluppertestapplication.adapter.ShowStoreListAdapter
import com.live.fluppertestapplication.colorPickerDialog.colorpickerAdapter.MaterialColorPickerAdapter
import com.live.fluppertestapplication.databinding.ActivityProductDetailBinding
import com.live.fluppertestapplication.db.Product
import com.live.fluppertestapplication.utils.AppConstants
import com.live.fluppertestapplication.utils.ConvertorClass
import com.live.fluppertestapplication.viewModel.StoreBean
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.productImage
import kotlinx.android.synthetic.main.activity_product_detail.rvSelectedColorList
import kotlinx.android.synthetic.main.activity_product_detail.rvSelectedStoreList

class ProductDetailActivity :AppCompatActivity(),View.OnClickListener {
    lateinit var activityProductDetailBinding: ActivityProductDetailBinding
    lateinit var  productData:Product
    lateinit var selectedColorList:ArrayList<String>
    lateinit var selectedStoresList:ArrayList<StoreBean>
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)
        activityProductDetailBinding.lifecycleOwner = this
        getIntentData()
        viewInit()
       // activityProductDetailBinding.productDetailData=p
    }
    private fun getIntentData() {
        val product=intent.getStringExtra(AppConstants.PRODUCT_DATA)
        if (product!=null) {
            productData=ConvertorClass.fromStringToObject(product)
            selectedColorList=ConvertorClass.fromStringToArrayList(productData.colors)
            var selectedStoreMap:HashMap<String,ArrayList<StoreBean>> = HashMap()
            selectedStoreMap=ConvertorClass.fromStringToHashMap(productData.stores)
            selectedStoresList = selectedStoreMap.getValue(AppConstants.SELECTED_STORE_KEY)
            activityProductDetailBinding.productDetailData=productData
        }
    }
    private fun viewInit() {
        Glide.with(this)
            .load(productData.product_image)
            .placeholder(R.drawable.placeholder)
            .into(productImage)
        rvSelectedColorList.adapter = MaterialColorPickerAdapter(selectedColorList,false)
        ivBack.setOnClickListener(this)
        AddEditProductActivity.showStoreListAdapter = ShowStoreListAdapter(this, selectedStoresList)
        rvSelectedStoreList.adapter = AddEditProductActivity.showStoreListAdapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack->{
                finish()
            }
        }
    }
}