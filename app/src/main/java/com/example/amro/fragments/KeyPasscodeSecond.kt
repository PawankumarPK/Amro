package com.example.amro.fragments


import com.example.amro.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.amro.fragments.BaseFragment
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.fragment_key_passcode_second.*


class KeyPasscodeSecond : BaseFragment() {

    var number: Long = 0

    private fun updateNumber(digit: Int) {
        number = number * 10 + digit
        mEditText.text = number.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_key_passcode_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity.mToolbar.visibility = View.GONE

        val numButtons = arrayOf(mZero, mOne, mTwo, mThree, mFour,
                mFive, mSix, mSeven, mEight, mNine)
        for (i in 0..numButtons.size - 1) {
            numButtons[i].setOnClickListener { updateNumber(i) }
        }

        mCancel.setOnClickListener { cancel() }
        mClear.setOnClickListener { clear() }
        mDone.setOnClickListener { mDone() }
    }

    private fun cancel() {
        fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, CancelAndContinue()).addToBackStack(null).commit()
    }

    private fun clear() {
        number *= 0
        mEditText.text = ""
    }

    private fun mDone() {

        if (mEditText.text.toString().equals("123")) {
            Toast.makeText(context, "Cancel Delivery", Toast.LENGTH_SHORT).show()
            val inv = StartFragment()
            //inv.setTalkerListener(myTalker,myListener)
            //myTalker.setCommand(1)
            fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, inv).addToBackStack(null).commit()
            mEditText.text = ""
            number *= 0

        } else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            mEditText.text = ""
            number *= 0
        }
    }


}
