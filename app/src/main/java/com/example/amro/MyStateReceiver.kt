package com.example.amro

import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.TripDetails
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.view.ViewPager

/**
 * Created by ajayvishnu on 22/06/19.
 */
class MyStateReceiver: BroadcastReceiver() {

    private var mPager: ViewPager? = null

    private var laststate:Int = -1

    private val pageStateArray:Map<Int, Int> = mapOf<Int,Int>(
            20 to FragmentsAdapter.Screens.Start.ordinal,
            30 to FragmentsAdapter.Screens.Progress.ordinal,
            40 to FragmentsAdapter.Screens.ScanIn.ordinal,
            50 to FragmentsAdapter.Screens.Floors.ordinal,
            60 to FragmentsAdapter.Screens.Break.ordinal,
            70 to FragmentsAdapter.Screens.Break.ordinal,
            80 to FragmentsAdapter.Screens.Receive.ordinal,
            90 to FragmentsAdapter.Screens.Progress.ordinal,
            100 to FragmentsAdapter.Screens.ScanOut.ordinal,
            110 to FragmentsAdapter.Screens.DeliveryDone.ordinal,
            120 to FragmentsAdapter.Screens.DeliveryDone.ordinal,
            130 to FragmentsAdapter.Screens.ContinueNav.ordinal
    )

    fun setPager(pager: ViewPager) {
        mPager = pager
    }

    override fun onReceive(context: Context, intent: Intent) {
        val error = intent.getBooleanExtra("error",false)
        if(!error) {
            val state = intent.getIntExtra("state", -1)
            val tripId = intent.getIntExtra("tripid", -1)
            if (state >= 0) {
                TripDetails.TripState = state
                TripDetails.TripId = tripId
                if (pageStateArray.containsKey(state) && state != laststate) {
                    mPager!!.currentItem = pageStateArray.getValue(state)
                    laststate = state
                }
            }
        }
        else {
            mPager!!.currentItem = FragmentsAdapter.Screens.Progress.ordinal
            laststate = -1
        }
    }
}

