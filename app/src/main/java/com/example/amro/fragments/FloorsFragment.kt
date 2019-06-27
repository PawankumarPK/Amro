package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.models.FloorRoomModels.FloorListModel
import com.example.amro.api.models.FloorRoomModels.FloorModel
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.confirmation_dialog.*
import kotlinx.android.synthetic.main.fragment_floor_number.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FloorsFragment : BaseFragment() {

    private lateinit var mDialog: Dialog
    private lateinit var floors:ArrayList<FloorModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_floor_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialog = Dialog(baseActivity)
        //mCancel.setOnClickListener { dialogBox() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getFloorButtons()
        }
    }

    private fun getFloorButtons() {
        val api = RetrofitClient.apiService
        val call = api.getFloorRoomList()

        call.enqueue(object : Callback<FloorListModel> {
            override fun onFailure(call: Call<FloorListModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FloorListModel>, response: Response<FloorListModel>) {
                floors = response.body().floors!!
                addFloorButtons()
            }
        })
    }

    private fun addFloorButtons() {
        val constraintLayout = btnContainer as GridLayout
        val floorCount = floors.size
        constraintLayout.removeAllViews()
        constraintLayout.rowCount = Math.ceil(floorCount.toDouble() / 5).toInt()
        constraintLayout.columnCount = 5

        for (floor in floors.iterator()) {
            val button = LayoutInflater.from(context).inflate(R.layout.floor_button, null) as Button
            button.text = floor.floorName
            button.setOnClickListener { floorBtnClick(floor) }
            constraintLayout.addView(button)
        }
    }

    private fun floorBtnClick(floor:FloorModel) {
        (FragmentsAdapter.Screens.Rooms.screen as RoomsFragment).setFloorData(floor)
        pagerRef.currentItem = FragmentsAdapter.Screens.Rooms.ordinal
    }

    private fun dialogBox() {
        val layout = LayoutInflater.from(baseActivity).inflate(R.layout.confirmation_dialog, null, false)
        mDialog.setContentView(layout)
        mDialog.mDone.setOnClickListener {
            val inv = StartFragment()
            //inv.setTalkerListener(myTalker, myListener)
            //fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, inv).addToBackStack(null).commit()
            mDialog.dismiss()
        }
        mDialog.mDiscard.setOnClickListener { mDialog.dismiss() }
        mDialog.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
        mDialog.window.decorView.systemUiVisibility = baseActivity.window.decorView.systemUiVisibility
        mDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

}