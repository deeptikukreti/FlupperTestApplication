package com.live.fluppertestapplication.viewModel

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.live.fluppertestapplication.activity.AddEditProductActivity
import com.live.fluppertestapplication.db.Product
import com.live.fluppertestapplication.db.ProductRepository
import com.live.fluppertestapplication.utils.AppConstants.SELECTED_STORE_KEY
import com.live.fluppertestapplication.utils.ConvertorClass
import com.live.fluppertestapplication.utils.Event
import kotlinx.coroutines.launch

class ProductViewModel(var repository: ProductRepository) : ViewModel(), Observable {
    val productList = repository.getAllProducts
    var isEditProduct=false
    var isFirstTime=true
    private val statusMessage = MutableLiveData<Event<String>>()
    private lateinit var product: Product
    val message: LiveData<Event<String>>
        get() = statusMessage


    @Bindable
    val heading = MutableLiveData<String>()

    @Bindable
    val productName = MutableLiveData<String>()

    @Bindable
    val productDescription = MutableLiveData<String>()

    @Bindable
    val productRegularPrice = MutableLiveData<String>()

    @Bindable
    val productSalePrice = MutableLiveData<String>()

    @Bindable
    val imagePath = MutableLiveData<String>()

    @Bindable
    val colorsList = MutableLiveData<String>()

    @Bindable
    val storeList = MutableLiveData<String>()


    fun addEditProduct(){
        if(checkValidation()){
            if(isEditProduct){
                product.name=productName.value!!
                product.description=productDescription.value!!
                product.regular_price=productRegularPrice.value!!
                product.sale_price=productSalePrice.value!!
                product.product_image = imagePath.value!!
                product.colors = colorsList.value!!
                product.stores = storeList.value!!
                update(product)
            }else {
                insert(
                    Product(
                        id = 0,
                        name = productName.value!!,
                        description = productDescription.value!!,
                        regular_price = productRegularPrice.value!!,
                        sale_price = productSalePrice.value!!,
                        product_image = imagePath.value!!,
                        colors = colorsList.value!!,
                        stores = storeList.value!!
                    )
                )
            }
        }

    }
    /*****************check that none of the field remain empty********************/
    fun checkValidation():Boolean{
        if(productName.value==null||productName.value.toString().trim()==""){
            statusMessage.value = Event("Please enter product name")
            return false
        }
        else if(productDescription.value==null||productDescription.value.toString().trim()==""){
            statusMessage.value = Event("Please enter product description")
            return false
        }
        else if(productRegularPrice.value==null||productRegularPrice.value.toString().trim()==""){
            statusMessage.value = Event("Please enter product regular price")
            return false
        }
        else if(productSalePrice.value==null||productSalePrice.value.toString().trim()==""){
            statusMessage.value = Event("Please enter product sale price")
            return false
        }
        else if(colorsList.value==null){
            statusMessage.value = Event("Please Select Any Color")
            return false
        }
        else if(storeList.value==null){
            statusMessage.value = Event("Please Select Any Store")
            return false
        }
        else if(imagePath.value==null){
            statusMessage.value = Event("Please Select Image")
            return false
        }
        return true
    }

    /**********set value for  product to be update ***********/

    fun editProduct(product: Product){
        isEditProduct=true
        heading.value="Edit Product"
        this.product=product
        productName.value = product.name
        productDescription.value = product.description
        productRegularPrice.value = product.regular_price
        productSalePrice.value = product.sale_price
        colorsList.value = product.colors
        imagePath.value = product.product_image
        storeList.value = product.stores
        var selectedStoreMap:HashMap<String,ArrayList<StoreBean>> = HashMap()
        AddEditProductActivity.selectedColorList = ConvertorClass.fromStringToArrayList(product.colors)
        selectedStoreMap=ConvertorClass.fromStringToHashMap(product.stores)
        AddEditProductActivity.selectedStoresList = selectedStoreMap.getValue(SELECTED_STORE_KEY)
        AddEditProductActivity.finalImagePath=product.product_image
    }

    /********add or create new product****/
    fun insert(product: Product) = viewModelScope.launch {
        val newRowId = repository.insert(product)
        if (newRowId > -1) {
                clearAllFieldsData()
            statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
    /********add or create dummy  product very first time****/
    fun insertFirstTime(product: Product) = viewModelScope.launch {
        val newRowId = repository.insert(product)
        if (newRowId > -1) {
            statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
   /**********edit or update product***********************/
    fun update(product: Product) = viewModelScope.launch {
        val noOfRows = repository.update(product)
        if (noOfRows > 0) {
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }

    }
    /***************clear fields after adding a new product ****************/
    fun clearAllFieldsData(){
        productName.value = ""
        productDescription.value = ""
        productRegularPrice.value = ""
        productSalePrice.value = null
        colorsList.value = null
        storeList.value = null
        imagePath.value = null
        AddEditProductActivity.finalImagePath=""
        AddEditProductActivity.selectedColorList.clear()
        AddEditProductActivity.selectedStoresList.clear()
        AddEditProductActivity.colorPickerAdapter.notifyDataSetChanged()
        AddEditProductActivity.showStoreListAdapter.notifyDataSetChanged()
        AddEditProductActivity.productImageView.visibility=View.GONE
    }
      /*********delete specific product*********************/
    fun delete(product: Product) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(product)
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}