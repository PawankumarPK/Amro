package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.UserModels.UserModel
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_key_passcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DispatchAuthFragment() : BaseFragment() {

    var number: Long = 0
    lateinit var mDialog: Dialog

    private fun updateNumber(digit: Int) {
        number = number * 10 + digit
        mEditText.text = number.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_key_passcode, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialog = Dialog(baseActivity)

        val numBtns = arrayOf(mZero, mOne, mTwo, mThree, mFour,
                mFive, mSix, mSeven, mEight, mNine)
        for (i in 0..numBtns.size - 1)
            numBtns[i].setOnClickListener { updateNumber(i) }

        mCancel.setOnClickListener { onCancel() }
        mDone.setOnClickListener { onDone() }
        mClear.setOnClickListener { onClear() }
    }

    private fun onClear() {
        number = 0
        mEditText.text = ""
    }

    private fun onCancel() {
        pagerRef.currentItem = FragmentsAdapter.Screens.Start.ordinal
    }

    private fun onDone() {

        val api = RetrofitClient.apiService
        val call = api.dispatch(mEditText.text.toString().toInt(), TripDetails.TripId)

        mProgressLayout.visibility = View.VISIBLE

        call.enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>?, t: Throwable?) {
                mProgressLayout.visibility = View.GONE
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    val userData = response.body()!!
                    if (userData.userId != -1) {
                        TripDetails.UserName = userData.userName!!
                        TripDetails.UserId = userData.userId!!
                    } else {
                        mProgressLayout.visibility = View.GONE
                        Toast.makeText(context, "Invalid Passcode", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        mEditText.text = ""
        number *= 0

    }

}