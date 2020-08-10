package com.live.fluppertestapplication.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.live.fluppertestapplication.R
import com.live.fluppertestapplication.adapter.ShowStoreListAdapter
import com.live.fluppertestapplication.adapter.StoreListAdapter
import com.live.fluppertestapplication.colorPickerDialog.ColorPickerDialog
import com.live.fluppertestapplication.colorPickerDialog.colorpickerAdapter.MaterialColorPickerAdapter
import com.live.fluppertestapplication.databinding.ActivityAddEditProductBinding
import com.live.fluppertestapplication.db.ProductDatabase
import com.live.fluppertestapplication.db.ProductRepository
import com.live.fluppertestapplication.utils.AppConstants.IS_EDIT_PRODUCT
import com.live.fluppertestapplication.utils.AppConstants.PRODUCT_DATA
import com.live.fluppertestapplication.utils.AppConstants.SELECTED_STORE_KEY
import com.live.fluppertestapplication.utils.ConvertorClass
import com.live.fluppertestapplication.utils.ImageUtils
import com.live.fluppertestapplication.viewModel.ProductViewModel
import com.live.fluppertestapplication.viewModel.ProductViewModelFactory
import com.live.fluppertestapplication.viewModel.StoreBean
import kotlinx.android.synthetic.main.activity_add_edit_product.*
import kotlinx.android.synthetic.main.dialog_select_store.*

private const val REQ_CHOOSE_IMAGE = 100

class AddEditProductActivity : BaseActivity(), View.OnClickListener {
    private lateinit var activityAddEditProductBinding: ActivityAddEditProductBinding
    private lateinit var productViewModel: ProductViewModel
    private var imgPath: String = ""
    private val permissions = arrayOf(Manifest.permission.CAMERA)
    lateinit var storeList:ArrayList<StoreBean>
    companion object {
        lateinit var colorPickerAdapter: MaterialColorPickerAdapter
        lateinit var showStoreListAdapter: ShowStoreListAdapter
        lateinit var selectedColorList: ArrayList<String>
        var selectedStoresList: ArrayList<StoreBean> = ArrayList()
        lateinit var productImageView: ImageView
        var finalImagePath = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)
        activityAddEditProductBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_edit_product)
        val dao = ProductDatabase.getInstance(application).productDAO
        val repository = ProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        activityAddEditProductBinding.myViewModel = productViewModel
        activityAddEditProductBinding.lifecycleOwner = this
        productImageView = productImage
        /***********************show toast messages*****************/
        productViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                if (productViewModel.isEditProduct) {
                    productViewModel.isEditProduct = false
                    startActivity(Intent(this,ShowProductsActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    finish()
                }
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
        setStoreListData()
        getIntentData()
        viewInitialization()
    }

    private fun setStoreListData() {
         storeList = ArrayList()
        storeList.add(StoreBean("1", "Store1"))
        storeList.add(StoreBean("2", "Store2"))
        storeList.add(StoreBean("3", "Store3"))
        storeList.add(StoreBean("4", "Store4"))
        storeList.add(StoreBean("5", "Store5"))     //5,6
        storeList.add(StoreBean("6", "Store6"))
        storeList.add(StoreBean("7", "Store7"))
        storeList.add(StoreBean("8", "Store8"))
    }

    /**********check intent data*************/
    private fun getIntentData() {
        if (intent.getBooleanExtra(IS_EDIT_PRODUCT, false)) {
            val product = intent.getStringExtra(PRODUCT_DATA)
            if (product != null) {
                productViewModel.editProduct(product = ConvertorClass.fromStringToObject(product))
                productImage.visibility = View.VISIBLE
                Glide.with(this)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .load(finalImagePath)
                    .placeholder(R.drawable.placeholder)
                    .into(productImage)
                for(i in storeList.indices){
                    for(j in selectedStoresList.indices){
                        if(storeList[i]== selectedStoresList[j]){
                            storeList[i].isSelected=true
                        }

                    }
                }
                colorPickerAdapter = MaterialColorPickerAdapter(selectedColorList, false)
                rvSelectedColorList.adapter = colorPickerAdapter
                showStoreListAdapter = ShowStoreListAdapter(this, selectedStoresList)
                rvSelectedStoreList.adapter = showStoreListAdapter
            }
        } else {
            productViewModel.heading.value = "Add Product"
        }
    }

    private fun viewInitialization() {
        txtAddImage.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
        selectColorTxt.setOnClickListener(this)
        selectStoreTxt.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txtAddImage -> {
                if (isPermissionsAllowed(permissions, true, REQ_CHOOSE_IMAGE)) {
                    chooseImage()
                }
            }
            R.id.selectColorTxt -> {
                showColorDialog()
            }
            R.id.selectStoreTxt -> {
                openSelectStoreDialog()
            }
        }
    }

    /*******************
     * open color dialog*/

    private fun showColorDialog() {
        ColorPickerDialog
            .Builder(this@AddEditProductActivity)
            .setTitle("Choose Color")// Pass Activity Instance
            .setColors(                            // Pass Predefined Hex Color
                arrayListOf(
                    "#f6e58d", "#ffbe76", "#ff7979", "#badc58", "#dff9fb",
                    "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"
                )
            ).setColorListener { selectedColorsList ->

                Log.d("qwerty", selectedColorsList.toString())
                selectedColorList = selectedColorsList
                productViewModel.colorsList.value =
                    ConvertorClass.fromArrayListToString(selectedColorList)
                colorPickerAdapter = MaterialColorPickerAdapter(selectedColorsList, false)
                rvSelectedColorList.adapter = colorPickerAdapter
            }
            .show()
    }

    /********************
     * select store dialog*/
    fun openSelectStoreDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_select_store)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btnOkay.setOnClickListener {
            var selectedStoreMap:HashMap<String,ArrayList<StoreBean>> = HashMap()
            selectedStoreMap.set(SELECTED_STORE_KEY, selectedStoresList)
            productViewModel.storeList.value=ConvertorClass.fromHashMapToString(selectedStoreMap)
            showStoreListAdapter = ShowStoreListAdapter(this, selectedStoresList)
            rvSelectedStoreList.adapter = showStoreListAdapter
            dialog.dismiss()
        }
        dialog.rvStoresList.adapter =
            StoreListAdapter(this, storeList, object : StoreListAdapter.OnStoreItemClick {
                override fun selectedStoreItem(storeBean: StoreBean, isAdd: Boolean) {
                    if (isAdd) {
                        selectedStoresList.add(storeBean)
                    } else {
                        selectedStoresList.remove(storeBean)
                    }
                }

            })
        dialog.show()
    }

    /*******************************add image***********************************/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQ_CHOOSE_IMAGE -> {
                if (isAllPermissionsGranted(grantResults)) {
                    chooseImage()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_not_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun chooseImage() {
        startActivityForResult(ImageUtils.getPickImageIntent(this), REQ_CHOOSE_IMAGE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                imgPath = ImageUtils.imgPath ?: ""
                imgPath = ImageUtils.resizeAndCompressImageBeforeSend(this, imgPath)!!
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    finalImagePath = ImageUtils.imageOrientation(imgPath, this)!!
                } else {
                    finalImagePath = imgPath
                }
                productImage.visibility = View.VISIBLE
                Glide.with(this)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .load(finalImagePath)
                    .placeholder(R.drawable.placeholder)
                    .into(productImage)
                productViewModel.imagePath.value = finalImagePath

            } else {
                if (data?.data != null) {
                    ImageUtils.imageUri = data.data
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, ImageUtils.imageUri)
                    imgPath = ImageUtils.getRealPathFromURI(bitmap, this)!!
                    imgPath = ImageUtils.resizeAndCompressImageBeforeSend(this, imgPath)!!
                    finalImagePath = imgPath
                    productImage.visibility = View.VISIBLE
                    Glide.with(this)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .load(imgPath)
                        .placeholder(R.drawable.placeholder)
                        .into(productImage)
                    productViewModel.imagePath.value = finalImagePath
                }

            }
        }
    }


}