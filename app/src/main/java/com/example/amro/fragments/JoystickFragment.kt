package com.example.amro.fragments


import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_joystick.*

class JoystickFragment : BaseFragment() { //, JoystickView.JoystickListener {

    private val TAG = "JoystickFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_joystick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCancel.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Start.ordinal
        }
    }

    /*
    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        Log.i("----->",x.toString())
        when (id) {
            R.id.mJoystick -> Log.d(TAG, "X percent: $xPercent Y percent: $yPercent")
        }
    }
    */

}

