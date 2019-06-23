package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.api.RetrofitClient
import com.example.amro.api.StandardModels.STATUS
import com.example.amro.api.TripDetails
import com.example.amro.api.UserModels.UserModel
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


class PassCodeFragment : BaseFragment() {

    var number: Long = 0
    lateinit var mDialog: Dialog
     lateinit var status : STATUS

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
        fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, StartFragment()).commit()
    }

    private fun showProgressFragment() {
        mProgressLayout.visibility = View.VISIBLE
        mLinearLayout.visibility = View.GONE
        mCancel.visibility = View.GONE
    }


    private fun loadBack() {
        mProgressLayout.visibility = View.GONE
        mLinearLayout.visibility = View.VISIBLE
        mCancel.visibility = View.VISIBLE
    }

    private fun loadNext() {
        mProgressLayout.visibility = View.GONE
        TripDetails.TripId.toString()
        fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, InventoryFragment()).commit()
    }

    private fun onDone() {

        val api = RetrofitClient.apiService
        val call = api.checkPasscode(mEditText.text.toString())

        call.enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>?, t: Throwable?) {
                loadBack()
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    val userData = response.body()!!
                    if (userData.Id != -1) {
                        TripDetails.UserId = userData.Id!!
                        TripDetails.TripId = userData.TripId!!
                        TripDetails.UserName = userData.Name!!
                        //TripDetails.setUserData(userData)
                        loadNext()
                    } else {
                        loadBack()
                        Toast.makeText(context, "Invalid Passcode", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        showProgressFragment()
        mEditText.text = ""
        number *= 0

    }

}