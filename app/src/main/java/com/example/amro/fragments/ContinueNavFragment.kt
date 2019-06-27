package com.example.amro.fragments


import com.example.amro.R
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.StandardModels.StdStatusModel
import kotlinx.android.synthetic.main.fragment_cancel_and_continue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContinueNavFragment : BaseFragment() {

    private lateinit var mDialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cancel_and_continue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialog = Dialog(baseActivity)
        //mCancelDelivery.setOnClickListener { confirmCancelNav() }
        mContinue.setOnClickListener {  continueNav() }
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
        //val inv = DeliveryAuthFragment()
        //inv.setTalkerListener(myTalker, myListener)
        //fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, inv).addToBackStack(null).commit()
        //mDialog.dismiss()
    }

}
