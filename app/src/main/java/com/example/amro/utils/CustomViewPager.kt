package com.example.amro.utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by ajayvishnu on 24/06/19.
 */
class CustomViewPager(context: Context, attr: AttributeSet) : ViewPager(context,attr) {

    private var swipeEnabled: Boolean = false

    init {
        this.swipeEnabled = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.swipeEnabled) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.swipeEnabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(enabled: Boolean) {
        this.swipeEnabled = enabled
    }
}