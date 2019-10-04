package com.example.amro.adapter

import com.example.amro.fragments.*
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager

/**
 * Created by ajayvishnu on 24/06/19.
 */

class FragmentsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    enum class Screens(val screen: BaseFragment) {


        Navigation(NavigationFragment()),
        Launch(LaunchFragment()),
        Joystick(JoystickFragment()),
        Battery(BatteryChgFragment()),
        Settings(SettingsFragment()),
        Trouble(TroubleFragment()),
        Start(StartFragment()),
        ScanIn(ScanInFragment()),
        Floors(FloorsFragment()),
        Rooms(RoomsFragment()),
        Progress(ProgressFragment()),
        Auth(AuthFragment()),
        Break(BreakFragment()),
        Receive(ReceiveFragment()),
        ScanOut(ScanOutFragment()),
        DeliveryDone(DeliveryComplete()),
        ContinueNav(ContinueNavFragment()),

    }

    constructor(fm: FragmentManager, pager: ViewPager) : this(fm) {
        enumValues<Screens>().forEach {

            it.screen.setPagerReference(pager)
        }
    }

    override fun getCount(): Int {
        return enumValues<Screens>().size
    }

    override fun getItem(position: Int): BaseFragment {
        return enumValues<Screens>().get(position).screen
    }
}