package com.example.amro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.view.ViewPager
import android.util.Log

/**
 * Created by ajayvishnu on 22/06/19.
 */
class MyStateReceiver: BroadcastReceiver() {

    var mPager: ViewPager? = null

    fun setPager(pager: ViewPager) {
        mPager = pager
    }

    override fun onReceive(context: Context, intent: Intent) {

        var state = intent.getIntExtra("state",0)

        Log.e("-=====",state.toString())

        val pageStateArray:Map<Int, Int> = mapOf<Int,Int>(
                20 to 0,
                40 to 2,
                40 to 4
        )

        mPager!!.currentItem = pageStateArray.getValue(state)
    }
}

