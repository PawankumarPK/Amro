package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.adapter.SettingsAdapter
import com.example.amro.api.DeviceStats
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : BaseFragment() {

    enum class SettingType() {
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

    private lateinit var adapter: SettingsAdapter

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    private val batteryInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            itemList.get(7)!!.value = level.toString() + "%"
            adapter.notifyDataSetChanged()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            updateSettings()
            adapter.notifyDataSetChanged()
        }
    }

    fun updateSettings() {
        itemList.get(0)!!.value = if(DeviceStats.ServerConnected) "1" else "0"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView.layoutManager = GridLayoutManager(context,3)
        adapter = SettingsAdapter(itemList)
        mRecyclerView.adapter = adapter

        activity!!.registerReceiver(this.batteryInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        settingsDoneBtn.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Start.ordinal
        }
    }
}
