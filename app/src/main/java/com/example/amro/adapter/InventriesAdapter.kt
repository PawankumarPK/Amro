package com.example.amro.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amro.R
import kotlinx.android.synthetic.main.view_holder.view.*


class InventriesAdapter(private val list: ArrayList<String>) : RecyclerView.Adapter<InventriesAdapter.viewHolder>() {

    private var mCounter = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false))
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.bindItems(list[position])
    }

    inner class viewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(position: String) {

            itemView.mMedicineName.text = position


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
