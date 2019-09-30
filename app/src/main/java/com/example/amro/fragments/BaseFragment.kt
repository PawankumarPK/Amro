package com.example.amro.fragments

import com.example.amro.activity.BaseActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager

open class BaseFragment : Fragment() {

     var baseActivity : BaseActivity?? = null

    lateinit var pagerRef:ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity
    }

    fun setPagerReference(ref:ViewPager) {
        pagerRef = ref
    }
}