package com.live.fluppertestapplication.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Product::class],version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDAO : ProductDAO
    companion object{
      @Volatile
      private var INSTANCE : ProductDatabase? = null
          fun getInstance(context: Context): ProductDatabase {
              synchronized(this){
                  var instance = INSTANCE
                      if(instance==null){
                          instance = Room.databaseBuilder(
                                 context.applicationContext,
                                 ProductDatabase::class.java,
                                 "product_data_table"
                          ).build()
                      }
                  return instance
              }
          }
    }

}

