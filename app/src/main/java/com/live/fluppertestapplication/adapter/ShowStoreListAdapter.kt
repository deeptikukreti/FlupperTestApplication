package com.live.fluppertestapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.fluppertestapplication.R
import com.live.fluppertestapplication.viewModel.StoreBean
import kotlinx.android.synthetic.main.item_store_layout.view.*
//,var storeList: ArrayList<String>
class ShowStoreListAdapter(var context: Context, var storeList:ArrayList<StoreBean>) :
    RecyclerView.Adapter<ShowStoreListAdapter.StoreViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_store_layout, parent, false)
        return StoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bindView(position).storeName.text=storeList[position].store_name
    }

    class StoreViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindView(position: Int): View {
            return itemView
        }
    }
    override fun getItemCount(): Int {
       return storeList.size
    }

}
