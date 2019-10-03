package com.example.amro.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.example.amro.activity.BaseActivity

open class BaseFragment : Fragment() {

    var baseActivity: BaseActivity?? = null

    lateinit var pagerRef: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity
    }

    fun setPagerReference(ref: ViewPager) {
        pagerRef = ref
    }
}