package com.example.amro

import com.example.amro.api.DeviceStats
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

/**
 * Created by ajayvishnu on 28/06/19.
 */
class BatteryInfoReceiver : BroadcastReceiver() {
    override fun onReceive(ctxt: Context, intent: Intent) {
        DeviceStats.ScreenBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
    }
}