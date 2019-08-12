package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.models.StandardModels.StdStatusModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_delivery_compl.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryComplete : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delivery_compl, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAgainBtn.setOnClickListener {
            startNewTrip()
            pagerRef.currentItem = FragmentsAdapter.Screens.Progress.ordinal
        }
    }

    fun setDeliveryStatusText(txt:String) {
        //mThanku.text = txt
    }

    private fun startNewTrip() {
        val api = RetrofitClient.apiService
        val call = api.newTrip()

        call.enqueue(object : Callback<StdStatusModel> {
            override fun onFailure(call: Call<StdStatusModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<StdStatusModel>, response: Response<StdStatusModel>) {
                var status = response.body().status!!
            }
        })
    }

}