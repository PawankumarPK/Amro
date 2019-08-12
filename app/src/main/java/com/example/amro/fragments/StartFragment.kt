package com.example.amro.fragments


import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_start_delivery.*

class StartFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStartDelivery.setOnClickListener {

            (FragmentsAdapter.Screens.Auth.screen as AuthFragment).setActionParams(
                    AuthFragment.AuthType.DispatchAuth, FragmentsAdapter.Screens.Start)
            pagerRef.currentItem = FragmentsAdapter.Screens.Auth.ordinal
        }

        mSettings.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Settings.ordinal
        }

        mManualOperate.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.SpeechToText.ordinal
        }

    }

}

