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
import com.example.amro.adapter.FragmentsAdapter
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


private const val TAG = "NavFrag"
private const val NONE = 0
private const val DRAG = 1
private const val ZOOM = 2


class NavigationFragment : BaseFragment() {

    private var goalInProgress = false

    private var matrix = Matrix()
    private var savedMatrix = Matrix()

    private var mode = NONE

    private var start = PointF()
    private var mid = PointF()
    private var oldDist = 1f

    private var goalRecieveMode = false
    private var mapLoader = MapLoader()
    private var fragmentIsVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        fragmentIsVisible = isVisibleToUser
        if (isVisibleToUser) {
            updateBatteryData()
            loadMap()
        } else {
            mapLoader.mapLoadHandler.removeCallbacksAndMessages(null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMode.setOnClickListener {
            modesSelect()
        }
        mCancel.setOnClickListener {
            pagerRef.currentItem = FragmentsAdapter.Screens.Start.ordinal
        }
        mForward.setOnClickListener {
            move("f")
        }
        mBackward.setOnClickListener {
            move("b")
        }
        mLeft.setOnClickListener {
            move("l")
        }
        mRight.setOnClickListener {
            move("r")
        }
        mStop.setOnClickListener {
            move("s")
        }

        mMapZoom.setOnTouchListener(TouchListener())

        loadAnimation()
    }

    private fun modesSelect() {

        goalRecieveMode = !goalRecieveMode
        if (goalRecieveMode) {
            mMode.text = "Cancel"
        } else {
            mMode.text = "Set Goal"
        }
    }

    private fun updateBatteryData() {
        val battery = DeviceStats.Battery.toString()
        mBattery.text = battery
    }

    private fun loadAnimation() {
        mLoading.startAnimation(AnimationUtils.loadAnimation(baseActivity, R.anim.rotate_forward))
    }

    private fun loadMap() {
        if (fragmentIsVisible) {
            updateBatteryData()
            updateBotXY()
            Picasso.get()
                .load(Helper.getConfigValue(baseActivity!!, "ros_url")!! + "/map")
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(mapLoader)
            Log.i(TAG, "Loading Map...")
        }
    }

    private fun move(direction: String) {
        val api = RetrofitClient.rosService
        val call = api.move(direction, seekSpeedLinear.progress, seekSpeedAngular.progress)

        call.enqueue(object : Callback<StdStatusModel> {
            override fun onFailure(call: Call<StdStatusModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                goalInProgress = false
            }

            override fun onResponse(call: Call<StdStatusModel>, response: Response<StdStatusModel>) {
                Toast.makeText(baseActivity, "Move Direction Set", Toast.LENGTH_SHORT).show()
                goalInProgress = false
            }
        })
    }

    private fun SendGoal(x: Float, y: Float) {

        goalInProgress = true

        val targetX: Float = x * 0.05F
        val targetY: Float = y * 0.05F
        mSource.x = x
        mSource.y = y

        val api = RetrofitClient.apiService
        val call = api.setGoalXY(targetX, targetY, TripDetails.TripId)

        call.enqueue(object : Callback<StdStatusModel> {
            override fun onFailure(call: Call<StdStatusModel>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                goalInProgress = false
            }

            override fun onResponse(call: Call<StdStatusModel>, response: Response<StdStatusModel>) {
                Toast.makeText(baseActivity, "Goal XY Set", Toast.LENGTH_SHORT).show()
                goalInProgress = false
            }
        })
    }

    private fun updateBotXY() {
        if (baseActivity != null && mBot != null) {
            mBot.x = LocationStats.x
            mBot.y = LocationStats.y
        }
    }

    inner class MapLoader : Target {

        var mapLoadHandler = Handler()

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if (fragmentIsVisible && bitmap != null && mMapZoom != null) {
                mMapZoom.setImageBitmap(bitmap)
                mLoading.clearAnimation()
                mLoading.visibility = View.GONE
                mapLoadHandler.postDelayed({
                    loadMap()
                }, 2000)
            }
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            mapLoadHandler.postDelayed({
                loadMap()
            }, 5000)
        }
    }

    inner class TouchListener : View.OnTouchListener {

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
            val names = arrayOf("DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?")
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

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (goalRecieveMode) {
                val x = event!!.x
                val y = event.y
                //if(!goalInProgress) SendGoal(x, y)
                val pts = floatArrayOf(event.x, event.y)

                savedMatrix.mapPoints(pts)

                Log.i(TAG, (pts[0] - start.x).toString() + " " + (pts[1] - start.y).toString() + " " + start.toString())
            } else {
                val view = v as ImageView
                view.scaleType = ImageView.ScaleType.MATRIX
                val scale: Float

                when (event!!.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        matrix.set(view.imageMatrix)
                        savedMatrix.set(matrix)
                        start.set(event.x, event.y)
                        mode = DRAG
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                        mode = NONE
                    }

                    MotionEvent.ACTION_POINTER_DOWN -> {
                        oldDist = spacing(event)
                        if (oldDist > 5f) {
                            savedMatrix.set(matrix)
                            midPoint(mid, event)
                            mode = ZOOM
                        }
                    }

                    MotionEvent.ACTION_MOVE ->
                        if (mode == DRAG) {
                            matrix.set(savedMatrix)
                            matrix.postTranslate(event.x - start.x, event.y - start.y)
                        } else if (mode == ZOOM) {
                            val newDist = spacing(event)
                            if (newDist > 5f) {
                                matrix.set(savedMatrix)
                                scale = newDist / oldDist
                                matrix.postScale(scale, scale, mid.x, mid.y)
                            }
                        }
                }

                view.imageMatrix = matrix
            }

            return true
        }

    }
}
