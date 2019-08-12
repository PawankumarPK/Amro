package com.example.amro.fragments


import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.StandardModels.StdStatusModel
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_cancel_and_continue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContinueNavFragment : BaseFragment(){

      fun updateNumber(digit: Int) {
        number = number * 10 + digit
    }

    var number: Long = 0
    private lateinit var mDialog: Dialog
    var roomName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cancel_and_continue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialog = Dialog(baseActivity)
        mCancelDelivery.setOnClickListener { cancelNav() }
        mContinue.setOnClickListener { continueNav() }
    }

    override fun onResume() {
        mData.text = roomName
        super.onResume()
    }

    private fun continueNav() {
        val api = RetrofitClient.apiService
        val call = api.setBreak(0, TripDetails.TripId)

        call.enqueue(object : Callback<StdStatusModel> {
            override fun onFailure(call: Call<StdStatusModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<StdStatusModel>, response: Response<StdStatusModel>) {

            }
        })
        pagerRef.currentItem = FragmentsAdapter.Screens.Progress.ordinal


    }

    private fun cancelNav() {
        (FragmentsAdapter.Screens.DeliveryDone.screen as DeliveryComplete)
                .setDeliveryStatusText("Delivery Cancelled")

        (FragmentsAdapter.Screens.Auth.screen as AuthFragment).setActionParams(
                AuthFragment.AuthType.CancelAuth, FragmentsAdapter.Screens.ContinueNav)

        pagerRef.currentItem = FragmentsAdapter.Screens.Auth.ordinal

    }

    private fun clearAuthPasscode(digit: Int) {
        number = number * 10 + digit
    }

    fun setRoomData(roomName: String = "") {
        this.roomName = roomName

    }


}
