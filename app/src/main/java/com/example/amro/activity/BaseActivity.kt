package com.example.amro.activity

import com.example.amro.*
import com.example.amro.adapter.FragmentsAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.utils.Helper
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {

    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager
    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE)

    private val receiver = MyStateReceiver()
    private val sbreceiver = BatteryInfoReceiver()
    private val filter = IntentFilter("ai.jetbrain.RobotState")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        startKioskMode()

        RetrofitClient.init(Helper.getConfigValue(this,"api_url")!!)
        RetrofitClient.initRosAPI(Helper.getConfigValue(this,"ros_url")!!)

        pager.adapter = FragmentsAdapter(supportFragmentManager, pager)
        pager.currentItem = FragmentsAdapter.Screens.Start.ordinal

        receiver.setPager(pager)

        Intent(this, StateManagerService::class.java).also { intent ->
            startService(intent)
        }
        Intent(this, DeviceManagerService::class.java).also { intent ->
            startService(intent)
        }
        Intent(this, LocationTrackService::class.java).also { intent ->
            startService(intent)
        }

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
        unregisterReceiver(sbreceiver)

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, filter)
        registerReceiver(sbreceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun startKioskMode() {
        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (mDevicePolicyManager.isDeviceOwnerApp(packageName)) {
            mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, arrayOf(packageName))
            startLockTask()
            val intentFilter = IntentFilter(Intent.ACTION_MAIN)
            intentFilter.addCategory(Intent.CATEGORY_HOME)
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
            mDevicePolicyManager.addPersistentPreferredActivity(mAdminComponentName,
                    intentFilter, ComponentName(packageName, BaseActivity::class.java.name))
            mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, true)
            mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    Integer.toString(BatteryManager.BATTERY_PLUGGED_AC
                            or BatteryManager.BATTERY_PLUGGED_USB
                            or BatteryManager.BATTERY_PLUGGED_WIRELESS))

            window.decorView.systemUiVisibility = flags

            window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    window.decorView.systemUiVisibility = flags
                } else {
                }
            }

        } else {
            // Please contact your system administrator
            window.decorView.systemUiVisibility = flags
        }
    }

}
