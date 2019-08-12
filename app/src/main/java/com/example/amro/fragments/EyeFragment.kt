package com.example.amro.fragments


import com.example.amro.R
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_eye.*
import java.util.*


class EyeFragment : BaseFragment() {

    private val eyeMaxX = 200F
    private val eyeMaxY = 400F
    private val defaultDuration = 300
    private val maxDuration = 3000

    val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_eye, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //moveEyeBall( mEyeBallLeft,200F,100F)
        //moveEyeBall( mEyeBallRight,200F,100F)

        randomMoves()
    }

    private fun randomMoves() {
        val rand = Random()
        val x = rand.nextFloat() * eyeMaxX
        val y = rand.nextFloat() * eyeMaxY
        val max = maxDuration - defaultDuration

        moveEyeBall(mEyeBallLeft, x, y)
        moveEyeBall(mEyeBallRight, x, y)

        Handler().postDelayed({
            randomMoves()
        }, (rand.nextInt(max) + defaultDuration).toLong())

    }

    private fun moveEyeBall(target: ImageView, targetX: Float, targetY: Float) {
        val animSetXY = AnimatorSet()
        val x = ObjectAnimator.ofFloat(target, "translationX", mEyeBallLeft.x, targetX)
        val y = ObjectAnimator.ofFloat(target, "translationY", mEyeBallLeft.y, targetY)
        animSetXY.playTogether(x, y)
        animSetXY.interpolator = LinearInterpolator()
        animSetXY.duration = defaultDuration.toLong()
        animSetXY.start()
    }

}
