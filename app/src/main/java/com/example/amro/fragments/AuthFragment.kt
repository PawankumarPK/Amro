package com.example.amro.fragments


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.UserModels.UserModel
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.passcode_error_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthFragment : BaseFragment() {

    var number: Long = 0
    lateinit var mDialog: Dialog
    lateinit var authType: AuthType
    lateinit var onCancelScreen: FragmentsAdapter.Screens

    enum class AuthType {
        DeliveryAuth,
        DispatchAuth,
        CancelAuth
    }

    private fun updateNumber(digit: Int) {
        number = number * 10 + digit
        mEditText.text = number.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val numBtns = arrayOf(
            mZero, mOne, mTwo, mThree, mFour,
            mFive, mSix, mSeven, mEight, mNine
        )
        for (i in 0 until numBtns.size)
            numBtns[i].setOnClickListener { updateNumber(i) }

        mCancel.setOnClickListener { onCancel() }
        mDone.setOnClickListener {
            if (mEditText.length() == 0)
                errorPasscodeDialog()
            else
                onDone()
        }
        mClear.setOnClickListener { onClear() }
    }


    private fun onClear() {
        number = 0
        mEditText.text = ""
    }

    private fun onCancel() {
        number = 0
        pagerRef.currentItem = onCancelScreen.ordinal
    }

    fun setActionParams(type: AuthType, onCancelScreen: FragmentsAdapter.Screens) {
        this.authType = type
        this.onCancelScreen = onCancelScreen
    }

    private fun getApiCall(): Call<UserModel> {

        val pin = mEditText.text.toString().toInt()
        val api = RetrofitClient.apiService
        val call: Call<UserModel>

        when (this.authType) {
            AuthType.DeliveryAuth -> call = api.delivery(pin, TripDetails.TripId)
            AuthType.DispatchAuth -> call = api.dispatch(pin, TripDetails.TripId)
            AuthType.CancelAuth -> call = api.cancelDelivery(pin, TripDetails.TripId)
        }

        return call
    }

    private fun onDone() {
        mProgressLayout.visibility = View.VISIBLE
        getApiCall().enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>?, t: Throwable?) {
                mProgressLayout.visibility = View.GONE

            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    val userData = response.body()!!
                    if (userData.userId != -1) {
                        //TripDetails.UserName = userData.userName!!
                        //TripDetails.UserId = userData.userId!!
                    } else {
                        mProgressLayout.visibility = View.GONE
                        errorPasscodeDialog()
                        //Toast.makeText(context, "Invalid Passcode", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })


    }

    private fun errorPasscodeDialog() {
        mDialog = Dialog(baseActivity)
        val layout = LayoutInflater.from(baseActivity).inflate(R.layout.passcode_error_dialog, null, false)
        mDialog.setContentView(layout)
        if (mEditText.length() == 0)
            mDialog.mIncorrectPasscode.text = "Please enter passcode"
        mDialog.mOK.setOnClickListener {
            number = 0
            mEditText.text = ""
            mDialog.dismiss()
        }
        mDialog.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
        mDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window.decorView.systemUiVisibility = baseActivity.window.decorView.systemUiVisibility
        mDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onStart() {
        super.onStart()
        onClear()
    }

}

