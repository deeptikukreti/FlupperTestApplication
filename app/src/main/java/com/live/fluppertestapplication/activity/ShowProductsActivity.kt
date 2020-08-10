package com.live.fluppertestapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.live.fluppertestapplication.R
import com.live.fluppertestapplication.adapter.ProductListAdapter
import com.live.fluppertestapplication.databinding.ActivityShowProductBinding
import com.live.fluppertestapplication.db.Product
import com.live.fluppertestapplication.db.ProductDatabase
import com.live.fluppertestapplication.db.ProductRepository
import com.live.fluppertestapplication.utils.AppConstants.IS_EDIT_PRODUCT
import com.live.fluppertestapplication.utils.AppConstants.PRODUCT_DATA
import com.live.fluppertestapplication.utils.ConvertorClass
import com.live.fluppertestapplication.viewModel.ProductViewModel
import com.live.fluppertestapplication.viewModel.ProductViewModelFactory

class ShowProductsActivity : AppCompatActivity() {
    private lateinit var activityShowProductBinding:ActivityShowProductBinding
    private lateinit var productViewModel: ProductViewModel
    lateinit var productListAdapter:ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityShowProductBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_product)
        val dao = ProductDatabase.getInstance(application).productDAO
        val repository = ProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this,factory).get(ProductViewModel::class.java)
        initView()

    }
    override fun onResume() {
        super.onResume()
        getProductList()
    }

    private fun initView() {
        productListAdapter= ProductListAdapter(this@ShowProductsActivity,object :
            ProductListAdapter.OnItemClick{
            override fun onSelectItemClick(
                product: Product,
                isEdit:Boolean) {
                if(isEdit){
                    startActivity(Intent(this@ShowProductsActivity,AddEditProductActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .putExtra(IS_EDIT_PRODUCT,true)
                        .putExtra(PRODUCT_DATA,ConvertorClass.fromObjectToString(product)))

                }else{
                    productViewModel.delete(product)
                }
            }
        })

    }

    private fun getProductList() {
        productViewModel.productList.observe(
            this,
            Observer<List<Product?>?> {
                productListAdapter.setList(it as ArrayList<Product>)
                activityShowProductBinding.rvProductList.adapter=productListAdapter
                productListAdapter.notifyDataSetChanged()
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}