package com.example.amro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import kotlinx.android.synthetic.main.fragment_receive.*


class ReceiveFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mReceiveDelivery.setOnClickListener {
            reciveDelivery()
        }
        mSettings.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Settings.ordinal
        }

    }

    private fun reciveDelivery() {
        (FragmentsAdapter.Screens.Auth.screen as AuthFragment).setActionParams(
            AuthFragment.AuthType.DeliveryAuth, FragmentsAdapter.Screens.Receive
        )
        pagerRef.currentItem = FragmentsAdapter.Screens.Auth.ordinal

    }

}
