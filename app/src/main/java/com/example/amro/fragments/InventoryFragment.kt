package com.example.amro.fragments


import com.example.amro.R
import com.example.amro.adapter.InventriesAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.StockModels.StockListModel
import com.example.amro.api.StockModels.StockModel
import com.example.amro.api.TripDetails
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.amro.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_inventries.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryFragment : BaseFragment() {

    private var itemList: ArrayList<StockModel> = ArrayList()
    private lateinit var adapter: InventriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inventries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getListInventryScanIn()


        mDone.setOnClickListener {
            Toast.makeText(context, TripDetails.TripId.toString(), Toast.LENGTH_SHORT).show()
        }


/*
        mRecyclerView.layoutManager = LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
        adapter = InventriesAdapter(itemList)
        mRecyclerView.adapter = adapter
*/

    }

    private fun loadNext() {

        fragmentManager.beginTransaction().replace(R.id.mFrameContainer, FloorsFragment()).commit()

    }


    private fun getListInventryScanIn() {

        val api = RetrofitClient.apiService
        val call = api.getStockList(1)

        call.enqueue(object : Callback<StockListModel> {
            override fun onFailure(call: Call<StockListModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<StockListModel>?, response: Response<StockListModel>?) {
                if (response!!.isSuccessful) {
                    itemList = response.body().medicineList!!
                    adapter = InventriesAdapter(itemList)
                    mRecyclerView.adapter = adapter
                    mRecyclerView.layoutManager = LinearLayoutManager(baseActivity)
                }
            }

        })


    }


}
