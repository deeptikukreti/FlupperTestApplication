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
class StoreListAdapter(var context: Context,var storeList:ArrayList<StoreBean>,var onStoreItemClick: OnStoreItemClick) :
    RecyclerView.Adapter<StoreListAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_store_layout, parent, false)
        return StoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bindView(position).storeName.text=storeList[position].store_name
        if(storeList[position].isSelected){
            holder.bindView(position).storeName.alpha=1f
        }else{
            holder.bindView(position).storeName.alpha=0.4f
        }
        holder.bindView(position).setOnClickListener {
            if(holder.bindView(position).storeName.alpha==1f){
                holder.bindView(position).storeName.alpha=0.4f
                onStoreItemClick.selectedStoreItem(storeList[position],false)
            }else{
                holder.bindView(position).storeName.alpha=1f
                onStoreItemClick.selectedStoreItem(storeList[position],true)
            }

        }

    }
    class StoreViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindView(position: Int): View {
            return itemView
        }
    }
    override fun getItemCount(): Int {
       return storeList.size
    }

    interface OnStoreItemClick{
        fun selectedStoreItem(storeBean: StoreBean,isAdd:Boolean)
    }
}
