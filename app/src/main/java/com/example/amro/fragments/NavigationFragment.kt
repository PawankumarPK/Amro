package com.example.amro.fragments

import com.example.amro.api.LocationStats
import com.example.amro.api.RetrofitClient
import com.example.amro.api.TripDetails
import com.example.amro.api.models.Navigation.RosMap
import com.example.amro.api.models.StandardModels.StdStatusModel
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_navigation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NavigationFragment : BaseFragment(), View.OnTouchListener {


    var sendingGoal_inProgress = false


    private val TAG = "Touch"
    private val MIN_ZOOM = 1f
    private val MAX_ZOOM = 1f

    // These matrices will be used to scale points of the image
    var matrix = Matrix()
    var savedMatrix = Matrix()

    // The 3 states (events) which the user is trying to perform
    val NONE = 0
    val DRAG = 1
    val ZOOM = 2
    var mode = NONE

    // these PointF objects are used to record the point(s) the user is touching
    var start = PointF()
    var mid = PointF()
    var oldDist = 1f


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.example.amro.R.layout.fragment_navigation, container, false)

        view.setOnTouchListener(View.OnTouchListener { v, event ->

            val x = event.x
            val y = event.y
            if (!sendingGoal_inProgress) SendGoal(x, y)

            Log.i("----->NavFrag", "$x $y")

            true
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTestZoom.setOnTouchListener(this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            LoadMap()
            updateBotXY()
        }
    }

    fun LoadMap() {
        val api = RetrofitClient.rosService
        val call = api.getMap()

        call.enqueue(object : Callback<RosMap> {
            override fun onFailure(call: Call<RosMap>?, t: Throwable?) {
                Toast.makeText(baseActivity, "Something went wrong", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<RosMap>, response: Response<RosMap>) {
                Toast.makeText(baseActivity, "Got Map", Toast.LENGTH_SHORT).show()
                Log.i("----->Map:", response.body().width.toString())
                updateMapUI(response.body()!!)
            }
        })
    }


    fun updateMapUI(mapData: RosMap) {
        infoMessage.text = ""

        var rows: Int = mapData.height!!.toInt()
        var cols: Int = mapData.width!!.toInt()

        var v: View = object : View(baseActivity!!.applicationContext) {
            override fun onDraw(canvas: Canvas?) {
                if (canvas != null) {
                    val paint = Paint()
                    paint.style = Paint.Style.FILL
                    var dVal = 0
                    var index = 0
                    for (j in 0..rows) {
                        for (i in 0..cols) {
                            index = i + (j * cols)
                            dVal = -1
                            if (index > 0 && index < mapData.data!!.size) dVal = mapData.data!![index]

                            when (dVal) {
                                -1 -> paint.color = Color.GRAY
                                0 -> paint.color = Color.BLACK
                                else -> paint.color = Color.parseColor("#0096CB")
                            }

                            canvas.drawPoint(i.toFloat(), j.toFloat(), paint)
                            canvas.save()
                        }
                    }
                }
                super.onDraw(canvas)
            }
        }
        v.layout(0, 0, cols, rows)
        canvasHolder.x = 0F
        canvasHolder.y = 0F

        canvasHolder.addView(v)
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

            override fun onResponse(call: Call<StdStatusModel>, response: Response<StdStatusModel>) {
                Toast.makeText(baseActivity, "Goal XY Set", Toast.LENGTH_SHORT).show()
                sendingGoal_inProgress = false
            }
        })
    }

    fun updateBotXY() {
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
        val view = v as ImageView
        view.scaleType = ImageView.ScaleType.MATRIX
        val scale: Float

        dumpEvent(event)
        // Handle touch events here...

        when (event.action and MotionEvent.ACTION_MASK) {

            // first finger down only
            MotionEvent.ACTION_DOWN -> {
                matrix.set(view.imageMatrix)
                savedMatrix.set(matrix)
                start.set(event.x, event.y)
                Log.d(TAG, "mode=DRAG") // write to LogCat
                mode = DRAG
            }

            // first finger lifted
            MotionEvent.ACTION_UP,

            MotionEvent.ACTION_POINTER_UP -> {
                // second finger lifted
                mode = NONE
                Log.d(TAG, "mode=NONE")
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                // first and second finger down
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
                    matrix.postTranslate(event.x - start.x, event.y - start.y) // create the transformation in the matrix  of points
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

        view.imageMatrix = matrix // display the transformation on screen
        return true // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    /** Show an event in the LogCat view, for debugging  */
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


}
