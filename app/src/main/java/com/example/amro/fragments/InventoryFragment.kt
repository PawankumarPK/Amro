package com.example.amro.fragments


import com.example.amro.R
import com.example.amro.adapter.InventriesAdapter
import ai.jetbrain.sar.api.TripDetails
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.amro.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_inventries.*

class InventoryFragment : BaseFragment() {

    private var itemList: ArrayList<String> = ArrayList()
    private lateinit var adapter: InventriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inventries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fn = FloorsFragment()
        //fn.setTalkerListener(myTalker, myListener)


        mDone.setOnClickListener {
            Toast.makeText(context, TripDetails.TripId.toString(),Toast.LENGTH_SHORT).show()
            /*fragmentManager!!.beginTransaction()
                    .setCustomAnimations( R.animator.enter_from_left, 0, 0, R.animator.enter_from_right)
                    .replace(R.id.mFrameContainer, fn).addToBackStack(null).commit()*/
        }




        mRecyclerView.layoutManager = LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
        adapter = InventriesAdapter(itemList)
        mRecyclerView.adapter = adapter
        /*myListener.getItemSubscriber().addMessageListener { message ->
            itemList.add(message.data)
            activity.runOnUiThread { adapter.notifyDataSetChanged() }
        }

        myListener.getLockStateSubscriber().addMessageListener { message ->
            if (message.data.compareTo(0) == 0) {
                myListener.getLockStateSubscriber().removeAllMessageListeners()
                activity.runOnUiThread {
                    fragmentManager!!.beginTransaction()
                            .setCustomAnimations(R.animator.enter_from_left, 0, 0, R.animator.enter_from_right)
                            .replace(R.id.mFrameContainer, fn).addToBackStack(null).commit()
                }
            }

        }*/

    }


}
