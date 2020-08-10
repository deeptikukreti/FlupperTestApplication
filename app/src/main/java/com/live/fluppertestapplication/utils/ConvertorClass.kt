package com.live.fluppertestapplication.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.live.fluppertestapplication.db.Product
import com.live.fluppertestapplication.viewModel.StoreBean
import java.lang.reflect.Type

class ConvertorClass{
    companion object{
        fun fromStringToArrayList(value: String?): ArrayList<String> {
            val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
            return Gson().fromJson(value, listType)
        }

        fun fromArrayListToString(list: ArrayList<String>): String {
            val gson = Gson()
            return gson.toJson(list)
        }

        fun fromStringToObject(value: String?): Product {
            val productData: Type = object : TypeToken<Product?>() {}.getType()
            return Gson().fromJson(value, productData)
        }

        fun fromObjectToString(productData:Product): String {
            val gson = Gson()
            return gson.toJson(productData)
        }

        fun fromStringToHashMap(value: String?): HashMap<String,ArrayList<StoreBean>> {
            val storeData: Type = object : TypeToken<HashMap<String,ArrayList<StoreBean>>?>() {}.getType()
            return Gson().fromJson(value, storeData)
        }

        fun fromHashMapToString(storeData:HashMap<String,ArrayList<StoreBean>>): String {
            val gson = Gson()
            return gson.toJson(storeData)
        }
    }
}