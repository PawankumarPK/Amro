package com.example.amro.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.amro.fragments.*


class FragmentsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    enum class Screens(val screen: BaseFragment) {


        Navigation(NavigationFragment()),
        Launch(LaunchFragment()),
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