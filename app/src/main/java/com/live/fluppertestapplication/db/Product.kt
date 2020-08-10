package com.live.fluppertestapplication.db

import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@Entity(tableName = "product_data_table")
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subscriber_id")
    var id: Int,
    @ColumnInfo(name = "product_name")
    var name: String,
    @ColumnInfo(name = "product_description")
    var description: String,
    @ColumnInfo(name = "product_regular_price")
    var regular_price: String,
    @ColumnInfo(name = "product_sale_price")
    var sale_price: String,
    @ColumnInfo(name = "product_image")
    var product_image: String,
    @ColumnInfo(name = "colors_list")
    var colors: String,
    @ColumnInfo(name = "selected_stores_list")
    var stores: String

)

