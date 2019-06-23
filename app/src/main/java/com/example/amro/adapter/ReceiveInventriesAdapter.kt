package com.example.amro.adapter


import com.example.amro.api.StockModels.StockModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amro.R
import kotlinx.android.synthetic.main.view_holder_receive_inv.view.*


class ReceiveInventriesAdapter(private val medList: ArrayList<StockModel>) : RecyclerView.Adapter<ReceiveInventriesAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_receive_inv, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return medList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.onBindView(position)
/*        val med = medList[position]
        holder.medicineName.text = med.medicinesName*/
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun onBindView(position: Int) {
            val medData = medList[position]
            itemView.mMedicineName.text = medData.stockName

        }
    }
}