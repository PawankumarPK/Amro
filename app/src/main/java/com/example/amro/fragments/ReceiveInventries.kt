package com.example.amro.fragments

import com.example.amro.R
import com.example.amro.adapter.ReceiveInventriesAdapter
import com.example.amro.api.RetrofitClient
import com.example.amro.api.StockModels.StockListModel
import com.example.amro.api.StockModels.StockModel
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_receive_inventries.*
import kotlinx.android.synthetic.main.thanku_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiveInventries : BaseFragment() {

    private var itemList: ArrayList<StockModel> = ArrayList()
    private lateinit var adapter: ReceiveInventriesAdapter
    private lateinit var mDialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_receive_inventries, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDialog = Dialog(baseActivity)

        val fn = FloorsFragment()
        //fn.setTalkerListener(myTalker, myListener)

        val api = RetrofitClient.apiService
        val call = api.getStockList(1)


        call.enqueue(object : Callback<StockListModel> {
            override fun onFailure(call: Call<StockListModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<StockListModel>, response: Response<StockListModel>) {
                if (response.isSuccessful) {
                    itemList = response.body().medicineList!!
                    adapter = ReceiveInventriesAdapter(itemList)
                    mRecyclerView.adapter = adapter
                    mRecyclerView.layoutManager = LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
                    //activity.runOnUiThread { adapter.notifyDataSetChanged() }
                }
            }
        })

        mDone.setOnClickListener {
            /*fn.enterTransition = Slide(Gravity.RIGHT)
            fn.exitTransition = Slide(Gravity.LEFT)*/
            //dialogBox()
        }


    }

    private fun dialogBox() {

        val layout = LayoutInflater.from(baseActivity).inflate(R.layout.thanku_dialog, null, false)
        mDialog.setContentView(layout)
        mDialog.mThanku.setOnClickListener {

            val inv = StartFragment()
            //inv.setTalkerListener(myTalker, myListener)
            fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.animator.enter_from_left, 0, 0, R.animator.enter_from_right)
                    .replace(R.id.mFrameContainer, inv).addToBackStack(null).commit()
            mDialog.dismiss()
        }
        mDialog.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
        mDialog.window.decorView.systemUiVisibility = baseActivity.window.decorView.systemUiVisibility
        mDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }


    private fun backArrow() {
        val inv = PassCodeFragment()
        //inv.setTalkerListener(myTalker, myListener)
        //myTalker.setCommand(1)
        fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, inv).addToBackStack(null).commit()

    }

}


