package com.example.amro.activity

import com.example.amro.MyStateReceiver
import com.example.amro.StateManagerService
import com.example.amro.api.RetrofitClient
import com.example.amro.fragments.*
import com.example.amro.utils.Helper
import android.app.ListFragment
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.amro.MyDeviceAdminReceiver
import com.example.amro.R
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.fragment_pager_list.*


class BaseActivity : AppCompatActivity() {

    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager
    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE)


    var mAdapter: MyAdapter? = null

    class MyAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        val NUM_ITEMS = 10

        override fun getCount(): Int {
            return NUM_ITEMS
        }

        override fun getItem(position: Int): Fragment? {
            return LayoutFragment.newInstance(position)
        }
    }

    class LayoutFragment : ListFragment() {
        internal var mNum: Int = 0

        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            mNum = if (getArguments() != null) getArguments().getInt("num") else 1
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup,
            savedInstanceState: Bundle
        ): View {
            val v = inflater.inflate(R.layout.fragment_pager_list, container, false)
            tvtext.text = "Fragment #$mNum"
            return v
        }

        override fun onActivityCreated(savedInstanceState: Bundle) {
            super.onActivityCreated(savedInstanceState)

            var a = ArrayList<String>()

            setListAdapter(
                ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1, a
                )
            )
        }

        override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
            Log.i("FragmentList", "Item clicked: $id")
        }

        companion object {
            internal fun newInstance(state: Int): Fragment {
                /*val f = ArrayListFragment()
                val args = Bundle()
                args.putInt("num", num)
                f.setArguments(args)
                */

                when (state) {
                    1 -> return PassCodeFragment()
                    2 -> return InventoryFragment()
                    3 -> return FloorsFragment()
                    4 -> return BreakFragment()
                    5 -> return KeyPasscodeSecond()
                    6 -> return ReceiveInventries()
                    7 -> return SettingsFragment()
                }

                return StartFragment()
            }
        }
    }

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

        RetrofitClient.init(Helper.getConfigValue(this, "api_url")!!)

        //loadFragment()

        mAdapter = MyAdapter(supportFragmentManager)
        pager.setAdapter(mAdapter)

        val filter = IntentFilter("ai.jetbrain.RobotState")
        val receiver = MyStateReceiver()
        registerReceiver(receiver, filter)
        receiver.setPager(pager)
        pager.currentItem = 0

        Intent(this, StateManagerService::class.java).also { intent ->
            startService(intent)
        }

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
            mDevicePolicyManager.addPersistentPreferredActivity(
                mAdminComponentName,
                intentFilter, ComponentName(packageName, BaseActivity::class.java.name)
            )
            mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, true)
            mDevicePolicyManager.setGlobalSetting(
                mAdminComponentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                Integer.toString(
                    BatteryManager.BATTERY_PLUGGED_AC
                            or BatteryManager.BATTERY_PLUGGED_USB
                            or BatteryManager.BATTERY_PLUGGED_WIRELESS
                )
            )

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
        supportFragmentManager.beginTransaction().replace(R.id.mFrameContainer, sd).addToBackStack(null).commit()
    }

}
