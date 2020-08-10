package com.live.fluppertestapplication.db

import com.live.fluppertestapplication.db.Product
import com.live.fluppertestapplication.db.ProductDAO

class ProductRepository(private val dao : ProductDAO) {

    val getAllProducts = dao.getAllProducts()

    suspend fun insert(product: Product):Long{
       return dao.insertProduct(product)
    }

    suspend fun update(product: Product):Int{
        return dao.updateProduct(product)
    }

    suspend fun delete(product: Product) : Int{
       return dao.deleteProduct(product)
    }

    suspend fun deleteAll() : Int{
        return dao.deleteAll()
    }
}