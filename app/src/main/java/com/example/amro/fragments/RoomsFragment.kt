package com.example.amro.fragments


import com.example.amro.R
import ai.jetbrain.sar.api.FloorRoomModels.FloorModel
import ai.jetbrain.sar.api.FloorRoomModels.RoomModel
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import com.example.amro.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_floor_room.*


class RoomsFragment : BaseFragment() {

    private lateinit var mDialog: Dialog
    private lateinit var floor:FloorModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_floor_room, container, false)
    }

    private fun transferFragment(room : RoomModel){
        val breakFragment = BreakFragment()
        val args = Bundle()
        args.putString("destination", room.roomName)
        //myTalker.setNavigation(room.roomName!!)
        //breakFragment.setTalkerListener(myTalker,myListener)
        breakFragment.arguments=  args
        fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.animator.enter_from_left, 0, 0, R.animator.enter_from_right)
                .addToBackStack(null)
                .replace(R.id.mFrameContainer, breakFragment)
                .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDialog = Dialog(baseActivity)
        addRoomButtons()
        mCancel.setOnClickListener {
            val inv = FloorsFragment()
            //inv.setTalkerListener(myTalker,myListener)
            fragmentManager!!.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mFrameContainer, inv)
                    .commit()
        }
    }

    fun setFloorData(floorData:FloorModel) {
        floor = floorData
    }

    fun addRoomButtons() {
        mFloor.text = floor.floorName
        for (room in floor.floorRooms!!.iterator()) {
            val constraintLayout = roomBtnContainer as GridLayout
            val button = LayoutInflater.from(context).inflate(R.layout.floor_button, null) as Button
            constraintLayout.addView(button)
            button.text = room.roomName
            button.setOnClickListener { transferFragment(room) }
        }
    }

}
