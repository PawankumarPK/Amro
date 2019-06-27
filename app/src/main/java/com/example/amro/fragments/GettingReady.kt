package com.example.amro.fragments

import com.example.amro.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class GettingReady : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.getting_ready,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}