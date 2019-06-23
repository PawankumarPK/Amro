package com.example.amro.fragments

import com.example.amro.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_break.*

class BreakFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_break, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //mData.text = this.arguments!!.getString("destination")
        //mBreak.setOnClickListener { breakOn() }
        //mDelivery.setOnClickListener { tempClick() } // to be removed
    }

    private fun tempClick() {
        val inv = ReceiveInventries()
        //inv.setTalkerListener(myTalker, myListener)
        //fragmentManager!!.beginTransaction().addToBackStack(null).replace(R.id.mFrameContainer, inv).commit()
    }

    private fun breakOn() {
        val args = Bundle()
        val inv = CancelAndContinue()
        args.putString("destination", mData.text.toString())

        //fragmentManager!!.beginTransaction().addToBackStack(null).replace(R.id.mFrameContainer, inv).commit()
    }

}

