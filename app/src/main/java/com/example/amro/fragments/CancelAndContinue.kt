package com.example.amro.fragments


import com.example.amro.R
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.amro.fragments.BaseFragment
import kotlinx.android.synthetic.main.confirmation_dialog.*
import kotlinx.android.synthetic.main.fragment_cancel_and_continue.*

class CancelAndContinue : BaseFragment() {

    private lateinit var mDialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cancel_and_continue, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val destination = this.arguments!!.getString("destination")
        mData.text = destination

        mDialog = Dialog(baseActivity)
        mCancelDelivery.setOnClickListener { confirmCancelNav() }
        mContinue.setOnClickListener {  continueNav(destination) }
    }

    private fun continueNav(des: String) {
        val args = Bundle()
        val inv = BreakFragment()
        //myTalker.setNavigation(des)
        //myTalker.setCommand(3)
        //inv.setTalkerListener(myTalker, myListener)
        inv.arguments = args
        args.putString("destination", mData.text.toString())
        fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, inv).addToBackStack(null).commit()
    }

    private fun cancelNav() {
        val inv = KeyPasscodeSecond()
        //inv.setTalkerListener(myTalker, myListener)
        fragmentManager!!.beginTransaction().replace(R.id.mFrameContainer, inv).addToBackStack(null).commit()
        mDialog.dismiss()
    }

    private fun confirmCancelNav() {
        val layout = LayoutInflater.from(baseActivity).inflate(R.layout.confirmation_dialog, null, false)
        mDialog.setContentView(layout)
        mDialog.mDone.setOnClickListener { cancelNav() }
        mDialog.mDiscard.setOnClickListener { mDialog.dismiss() }
        mDialog.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
        mDialog.window.decorView.systemUiVisibility = baseActivity.window.decorView.systemUiVisibility
        mDialog.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }


}
