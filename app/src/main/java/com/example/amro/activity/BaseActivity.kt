package com.example.amro.activity

import ai.jetbrain.sar.MyDeviceAdminReceiver
import com.example.amro.R
import ai.jetbrain.sar.api.RetrofitClient
import com.example.amro.fragments.StartFragment
import ai.jetbrain.sar.utils.Helper
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

class BaseActivity : AppCompatActivity() {

    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager
    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            if (oldHandler != null)
                oldHandler.uncaughtException(
                        paramThread,
                        paramThrowable
                )
            else
                System.exit(2)
        }

        startKioskMode()

        RetrofitClient.init(Helper.getConfigValue(this,"api_url")!!)

        loadFragment()
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

    private fun loadFragment() {
        val sd = StartFragment()
        fragmentManager.beginTransaction().replace(R.id.mFrameContainer, sd).addToBackStack(null).commit()
    }

}
