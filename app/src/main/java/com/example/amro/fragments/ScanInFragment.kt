package com.example.amro.fragments


import com.example.amro.R
import com.example.amro.adapter.InventoryAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.StockModels.StockListModel
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_inventries.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanInFragment : BaseFragment() {

    private lateinit var adapter: InventoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inventries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = InventoryAdapter()
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(baseActivity)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            updateInventory()
        }
    }

    private fun updateInventory() {
        if (userVisibleHint) {
            val api = RetrofitClient.apiService
            val call = api.getStockList(TripDetails.TripId)
            call.enqueue(object : Callback<StockListModel> {
                override fun onFailure(call: Call<StockListModel>?, t: Throwable?) {
                    Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        updateInventory()
                    }, 5000)
                }

                override fun onResponse(call: Call<StockListModel>?, response: Response<StockListModel>?) {
                    if (response!!.isSuccessful) {
                        //Log.e("----->",response.body().stockList!!.size.toString())
                        adapter.updateList(response.body().stockList!!)
                        adapter.notifyDataSetChanged()

                    }
                    Handler().postDelayed({
                        updateInventory()
                    }, 1000)

                }
            })
        }
    }
}

