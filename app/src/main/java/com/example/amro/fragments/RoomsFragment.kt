package com.example.amro.fragments


import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.FloorRoomModels.FloorModel
import com.example.amro.api.models.StandardModels.StdStatusModel
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import kotlinx.android.synthetic.main.fragment_floor_room.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RoomsFragment : BaseFragment() {

    private lateinit var mDialog: Dialog
    private lateinit var floor: FloorModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_floor_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialog = Dialog(baseActivity)

        mCancel.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Floors.ordinal
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            addRoomButtons()
        }
    }

    fun setFloorData(floorData: FloorModel) {
        floor = floorData
        Log.d("floorData",floorData.toString())

    }

    private fun addRoomButtons() {
        mFloor.text = floor.floorName
        val constraintLayout = roomBtnContainer as GridLayout
        constraintLayout.removeAllViews()
        for (room in floor.floorRooms!!.iterator()) {
            val button = LayoutInflater.from(context).inflate(R.layout.floor_button, null) as Button
            constraintLayout.addView(button)
            button.text = room.roomName
            Log.d("valueX",room.roomName.toString())
            button.setOnClickListener {
                (FragmentsAdapter.Screens.Break.screen as BreakFragment).setRoomData(room.roomName!!)
                sendGoal(room.roomId!!)
                pagerRef.currentItem = FragmentsAdapter.Screens.Progress.ordinal
            }
        }
    }

    private fun sendGoal(roomId: Int) {
        val api = RetrofitClient.apiService
        val call = api.setGoal(roomId, TripDetails.TripId)

        call.enqueue(object : Callback<StdStatusModel> {
            override fun onFailure(call: Call<StdStatusModel>?, t: Throwable?) {
                //Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<StdStatusModel>, response: Response<StdStatusModel>) {
                // floors = response.body().floors!!
                // addFloorButtons()
            }
        })
    }

}
