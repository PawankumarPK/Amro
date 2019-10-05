package com.example.amro.fragments


import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.example.amro.R
import com.example.amro.api.DeviceStats
import com.example.amro.api.LocationStats
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.StandardModels.StdStatusModel
import com.example.amro.utils.Helper
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.fragment_navigation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NavigationFragment : BaseFragment(), View.OnTouchListener {

    var sendingGoal_inProgress = false
    val TAG = "NavFrag"

    var matrix = Matrix()
    var savedMatrix = Matrix()

    val NONE = 0
    val DRAG = 1
    val ZOOM = 2
    var mode = NONE

    var start = PointF()
    var mid = PointF()
    var oldDist = 1f

    var control_mode = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getBatteryData()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMapZoom.setOnTouchListener(this)
        LoadMap()
        updateBotXY()
        loadAnimation()

    }

    private fun getBatteryData() {
        val battery = DeviceStats.Battery
        mBattery.text = battery.toString()
    }

    private fun loadAnimation() {
        val anim = AnimationUtils.loadAnimation(baseActivity, R.anim.rotate_forward)
        mLoading.startAnimation(anim)

    }

    private var target: Target = object : Target {
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if (bitmap != null) {
                mMapZoom.setImageBitmap(bitmap)
                mLoading.clearAnimation()
                mLoading.visibility = View.GONE
            }

            Handler().postDelayed({
                LoadMap()
            }, 2000)
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            Handler().postDelayed({
                LoadMap()
            }, 5000)
        }

    }

    fun LoadMap() {
        Picasso.get().load(Helper.getConfigValue(baseActivity!!, "ros_url")!! + "/map")
            .memoryPolicy(MemoryPolicy.NO_CACHE).into(target)
        Log.i(TAG, "Loading Image...")

    }

    fun SendGoal(x: Float, y: Float) {

        sendingGoal_inProgress = true

        val api = RetrofitClient.apiService

        val targetX: Float = x * 0.05F
        val targetY: Float = y * 0.05F

        mSource.x = x
        mSource.y = y

        val call = api.setGoalXY(targetX, targetY, TripDetails.TripId)

        call.enqueue(object : Callback<StdStatusModel> {
            override fun onFailure(call: Call<StdStatusModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                sendingGoal_inProgress = false
            }

            override fun onResponse(
                call: Call<StdStatusModel>,
                response: Response<StdStatusModel>
            ) {
                Toast.makeText(baseActivity, "Goal XY Set", Toast.LENGTH_SHORT).show()
                sendingGoal_inProgress = false
            }
        })
    }

    private fun updateBotXY() {
        Thread {
            if (baseActivity != null)
                baseActivity!!.runOnUiThread {
                    if (mBot != null) {
                        mBot.x = LocationStats.x
                        mBot.y = LocationStats.y
                    }
                }

            Thread.sleep(1000)
            updateBotXY()

        }.start()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        //dumpEvent(event)

        if (control_mode == 1) {
            val x = event.x
            val y = event.y
            if (!sendingGoal_inProgress) SendGoal(x, y)
            Log.i(TAG, "$x $y")
        } else {
            val view = v as ImageView
            view.scaleType = ImageView.ScaleType.MATRIX
            val scale: Float

            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_DOWN -> {
                    matrix.set(view.imageMatrix)
                    savedMatrix.set(matrix)
                    start.set(event.x, event.y)
                    Log.d(TAG, "mode=DRAG") // write to LogCat
                    mode = DRAG
                }

                MotionEvent.ACTION_UP,

                MotionEvent.ACTION_POINTER_UP -> {
                    mode = NONE
                    Log.d(TAG, "mode=NONE")
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    oldDist = spacing(event)
                    Log.d(TAG, "oldDist=$oldDist")
                    if (oldDist > 5f) {
                        savedMatrix.set(matrix)
                        midPoint(mid, event)
                        mode = ZOOM
                        Log.d(TAG, "mode=ZOOM")
                    }
                }

                MotionEvent.ACTION_MOVE ->
                    if (mode == DRAG) {
                        matrix.set(savedMatrix)
                        matrix.postTranslate(
                            event.x - start.x,
                            event.y - start.y
                        ) // create the transformation in the matrix  of points
                    } else if (mode == ZOOM) {
                        // pinch zooming
                        val newDist = spacing(event)
                        Log.d(TAG, "newDist=$newDist")
                        if (newDist > 5f) {
                            matrix.set(savedMatrix)
                            scale = newDist / oldDist // setting the scaling of the
                            // matrix...if scale > 1 means
                            // zoom in...if scale < 1 means
                            // zoom out
                            matrix.postScale(scale, scale, mid.x, mid.y)
                        }
                    }
            }

            view.imageMatrix = matrix
        }

        return true
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    private fun dumpEvent(event: MotionEvent) {
        val names = arrayOf(
            "DOWN",
            "UP",
            "MOVE",
            "CANCEL",
            "OUTSIDE",
            "POINTER_DOWN",
            "POINTER_UP",
            "7?",
            "8?",
            "9?"
        )
        val sb = StringBuilder()
        val action = event.action
        val actionCode = action and MotionEvent.ACTION_MASK
        sb.append("event ACTION_").append(names[actionCode])

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action shr MotionEvent.ACTION_POINTER_ID_SHIFT)
            sb.append(")")
        }

        sb.append("[")
        for (i in 0 until event.pointerCount) {
            sb.append("#").append(i)
            sb.append("(pid ").append(event.getPointerId(i))
            sb.append(")=").append(event.getX(i).toInt())
            sb.append(",").append(event.getY(i).toInt())
            if (i + 1 < event.pointerCount)
                sb.append(";")
        }

        sb.append("]")
        Log.d("Touch Events ---------", sb.toString())
    }

}
