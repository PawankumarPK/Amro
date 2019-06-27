package com.example.amro.adapter

import com.example.amro.R
import com.example.amro.api.models.StockModels.StockModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.scanin_view_holder.view.*


class InventoryAdapter : RecyclerView.Adapter<InventoryAdapter.viewHolder>() {

    private var inventory: ArrayList<StockModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.scanin_view_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return inventory.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bindItems(position)
    }

    inner class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(position: Int) {
            val medData = inventory[position]
            itemView.mMedicineName.text = medData.stockName
            itemView.mMedicineQuantity.text = medData.stockQuantity.toString()
        }
    }

    fun updateList(list:ArrayList<StockModel>) {
        inventory = list
    }
}

