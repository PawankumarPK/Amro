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

        Ready       (GettingReady()),
        Settings    (SettingsFragment()),
        Start       (StartFragment()),
        DispatchAuth(DispatchAuthFragment()),
        ScanIn      (ScanInFragment()),
        Floors      (FloorsFragment()),
        Rooms       (RoomsFragment()),
        Progress    (ProgressFragment()),
        Break       (BreakFragment()),
        DeliveryAuth(DeliveryAuthFragment()),
        ScanOut     (ScanOutFragment()),
        DeliveryDone(DeliveryComplete())
    }

    constructor(fm: FragmentManager, pager:ViewPager): this(fm) {
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