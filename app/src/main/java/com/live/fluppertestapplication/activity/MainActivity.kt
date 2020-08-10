package com.live.fluppertestapplication.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.donedoob.utils.SharedPreferenceUtil
import com.live.fluppertestapplication.R
import com.live.fluppertestapplication.db.Product
import com.live.fluppertestapplication.db.ProductDatabase
import com.live.fluppertestapplication.db.ProductRepository
import com.live.fluppertestapplication.utils.AppConstants
import com.live.fluppertestapplication.utils.AppConstants.IS_EDIT_PRODUCT
import com.live.fluppertestapplication.utils.ConvertorClass
import com.live.fluppertestapplication.viewModel.ProductViewModel
import com.live.fluppertestapplication.viewModel.ProductViewModelFactory
import com.live.fluppertestapplication.viewModel.StoreBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() ,View.OnClickListener{
    private lateinit var productViewModel: ProductViewModel
     var colorsList:ArrayList<String> = ArrayList()
     var storesList:ArrayList<StoreBean> = ArrayList()
    var selectedStoreMap:HashMap<String,ArrayList<StoreBean>> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dao = ProductDatabase.getInstance(application).productDAO
        val repository = ProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        if(SharedPreferenceUtil.getInstance(this).isFirstTimeInstall!!){
            setDummyData()
        }
        initView()
    }

    private fun setDummyData() {
        SharedPreferenceUtil.getInstance(this).isFirstTimeInstall=false
        colorsList.add("#f6e58d")
        colorsList.add("#ffbe76")
        colorsList.add("#ff7979")
        val colors=ConvertorClass.fromArrayListToString(colorsList)
        storesList.add(StoreBean("1", "Store1"))
        storesList.add(StoreBean("2", "Store2"))
        storesList.add(StoreBean("3", "Store3"))
        selectedStoreMap.set(
            AppConstants.SELECTED_STORE_KEY,
            storesList
        )
        val stores=ConvertorClass.fromHashMapToString(selectedStoreMap)
        PopulateDbAsyncTask(productViewModel,colors,stores).execute()
    }

    private fun initView() {
        showProductBtn.setOnClickListener(this)
        createProductBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.showProductBtn->{
                startActivity(Intent(this,ShowProductsActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                finish()
            }
            R.id.createProductBtn->{
                startActivity(Intent(this,AddEditProductActivity::class.java)
                    .putExtra(IS_EDIT_PRODUCT,false)
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            }
        }
    }

     class PopulateDbAsyncTask(
         productViewModel: ProductViewModel,
         val colors: String,
        val stores: String
     ) :
        AsyncTask<Void?, Void?, Void?>() {
         var productViewModel: ProductViewModel=productViewModel

         override fun doInBackground(vararg params: Void?): Void? {
             productViewModel.insertFirstTime(Product(0,"Product1","Product1 description","200","150","https://4.imimg.com/data4/TM/FE/MY-2434106/ceramic-vases-500x500.jpg",colors ,stores))
             productViewModel.insertFirstTime(Product(0,"Product2","Product2 description","400","300","https://4.imimg.com/data4/TM/FE/MY-2434106/ceramic-vases-500x500.jpg",colors ,stores))
             productViewModel.insertFirstTime(Product(0,"Product3","Product3 description","600","400","https://4.imimg.com/data4/TM/FE/MY-2434106/ceramic-vases-500x500.jpg",colors ,stores))
             return null
         }

     }
}