package com.live.fluppertestapplication.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.live.fluppertestapplication.db.Product

@Dao
interface ProductDAO {
    @Insert
    suspend fun insertProduct(product: Product) : Long

    @Update
    suspend fun updateProduct(product: Product) : Int

    @Delete
    suspend fun deleteProduct(product: Product) : Int

    @Query("DELETE FROM product_data_table")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM product_data_table")
    fun getAllProducts():LiveData<List<Product>>
}