package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.api.models.FloorRoomModels.RoomModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_break.*

class BreakFragment : BaseFragment() {

    var roomName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_break, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //mBreak.setOnClickListener { breakOn() }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            mData.text = roomName
        }
    }

    fun setRoomData(roomData: RoomModel) {
        roomName = roomData.roomName!!
    }
    
    private fun breakOn() {

        //val inv = CancelAndContinue()
    }
    

}

