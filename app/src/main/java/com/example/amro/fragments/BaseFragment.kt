package com.example.amro.fragments

import com.example.amro.activity.BaseActivity
import android.app.Fragment
import android.os.Bundle

open class BaseFragment : Fragment() {

    lateinit var baseActivity : BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivity = activity as BaseActivity
    }
}