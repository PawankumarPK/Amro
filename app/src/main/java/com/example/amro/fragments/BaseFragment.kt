package com.example.amro.fragments

import com.example.amro.activity.BaseActivity
import android.os.Bundle
import android.support.v4.app.Fragment

open class BaseFragment : Fragment() {

    lateinit var baseActivity : BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivity = activity as BaseActivity
    }
}