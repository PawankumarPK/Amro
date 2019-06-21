package com.example.amro.adapter

import com.example.amro.api.StockModels.StockModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amro.R
import kotlinx.android.synthetic.main.view_holder.view.*


class InventriesAdapter(private val medListReciveInv: ArrayList<StockModel>) :
    RecyclerView.Adapter<InventriesAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false))
    }

    override fun getItemCount(): Int {

        return medListReciveInv.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.bindItems(position)
    }

    inner class viewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(position: Int) {
            val medData = medListReciveInv[position]

            itemView.mMedicineName.text = medData.stockName


        }


    }
}


/*
itemView.mIncrement.setOnClickListener {
    mCounter ++
    itemView.mValue.text = Integer.toString(mCounter)


}

itemView.mDecrement.setOnClickListener {
    mCounter --
    if(mCounter<0){
        mCounter=0
    }
    itemView.mValue.text = Integer.toString(mCounter)

}
*/
