package com.example.amro.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import kotlinx.android.synthetic.main.fragment_launch.*

class LaunchFragment : BaseFragment() {

    private val TAG = "LaunchFragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMapMode.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Navigation.ordinal

        }
        mGoalMode.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Navigation.ordinal

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

