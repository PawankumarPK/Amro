package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.adapter.SettingsAdapter
import com.example.amro.api.DeviceStats
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : BaseFragment() {

    private var loop = false

    enum class SettingType {
        Status,
        Progress
    }

    class SettingItem {
        var name:String = ""
        var type:SettingType
        var value:String = "0"

        constructor(name:String, type:SettingType=SettingType.Status) {
            this.name = name
            this.type = type
        }
    }

    private var itemList: Map<Int,SettingItem> = mapOf<Int,SettingItem>(
            0 to SettingItem("Server"),
            1 to SettingItem("Core"),
            2 to SettingItem("Database"),
            3 to SettingItem("Arduino1"),
            4 to SettingItem("Arduino2"),
            5 to SettingItem("Lidar"),
            6 to SettingItem("Battery",SettingType.Progress),
            7 to SettingItem("Screen",SettingType.Progress),
            8 to SettingItem("Sensors",SettingType.Progress)
    )

    private  var adapter: SettingsAdapter  = SettingsAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        loop = isVisibleToUser
        if (isVisibleToUser) {
            updateSettings()
            //activity!!.runOnUiThread(Runnable { adapter.notifyDataSetChanged() })
        }
    }

    fun updateSettings() {
        (itemList[0] ?: error("")).value = if(DeviceStats.ServerConnected) "1" else "0"
        (itemList[6] ?: error("")).value = "%.2fV".format(DeviceStats.Battery)
        (itemList[7] ?: error("")).value = DeviceStats.ScreenBattery.toString() + "%"

        if(activity!=null)
            activity!!.runOnUiThread(Runnable { adapter.notifyDataSetChanged() })

        if(loop) Handler().postDelayed({
            updateSettings()
        }, 3000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView.layoutManager = GridLayoutManager(context,3) as RecyclerView.LayoutManager
        mRecyclerView.adapter = adapter
        settingsDoneBtn.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Start.ordinal
        }
    }

}
