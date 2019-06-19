package com.example.amro.fragments


import com.example.amro.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amro.fragments.BaseFragment
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.fragment_start_delivery.*

class StartFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity.mToolbar.visibility = View.GONE

        mStartDelivery.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, PassCodeFragment()).commit()
        }
        mSettings.setOnClickListener { fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, SettingsFragment()).commit()}
    }


}

