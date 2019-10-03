package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.StandardModels.StdStatusModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_break.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BreakFragment : BaseFragment() {

    var roomName = ""

    private lateinit var getRoomData: RoomsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_break, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBreak.setOnClickListener {
            (FragmentsAdapter.Screens.ContinueNav.screen as ContinueNavFragment).setRoomData(mData.text.toString())
            breakOn()
        }
        getRoomData = RoomsFragment()

    }

    override fun onResume() {
        mData.text = roomName
        super.onResume()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
        }
    }


    fun setRoomData(roomName: String = "") {
        this.roomName = roomName

    }

    private fun breakOn() {
        val api = RetrofitClient.apiService
        val call = api.setBreak(1, TripDetails.TripId)

        call.enqueue(object : Callback<StdStatusModel> {
            override fun onFailure(call: Call<StdStatusModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<StdStatusModel>, response: Response<StdStatusModel>) {
            }
        })
        pagerRef.currentItem = FragmentsAdapter.Screens.Progress.ordinal
    }

}

