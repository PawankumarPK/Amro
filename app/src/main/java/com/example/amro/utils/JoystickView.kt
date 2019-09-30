package com.example.amro.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

class JoystickView : SurfaceView, SurfaceHolder.Callback, View.OnTouchListener {
    private var centerX: Float = 0.toFloat()
    private var centerY: Float = 0.toFloat()
    private var baseRadius: Float = 0.toFloat()
    private var hatRadius: Float = 0.toFloat()
    private  var joystickCallback: JoystickListener? = null
    private val ratio = 10

    private fun setupDimensions() {
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        baseRadius = (Math.min(width, height) / 3).toFloat()
        hatRadius = (Math.min(width, height) / 5).toFloat()
    }

    constructor(context: Context) : super(context) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener)
            joystickCallback = context
    }

    constructor(context: Context, attributes: AttributeSet, style: Int) : super(context, attributes, style) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener)
            joystickCallback = context
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener)
            joystickCallback = context
    }

    private fun drawJoystick(newX: Float, newY: Float) {
        if (holder.surface.isValid) {
            val myCanvas = this.holder.lockCanvas()
            val colors = Paint()
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            myCanvas.drawColor(Color.argb(255, 255, 255, 255))

            val hypotenuse =
                    Math.sqrt(Math.pow((newX - centerX).toDouble(), 2.0) + Math.pow((newY - centerY).toDouble(), 2.0))
                            .toFloat()
            val sin = (newY - centerY) / hypotenuse
            val cos = (newX - centerX) / hypotenuse

            colors.setARGB(255, 100, 100, 100)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)
            for (i in 1..(baseRadius / ratio).toInt()) {
                colors.setARGB(
                        150 / i,
                        255,
                        0,
                        0
                )
                myCanvas.drawCircle(
                        newX - cos * hypotenuse * (ratio / baseRadius) * i.toFloat(),
                        newY - sin * hypotenuse * (ratio / baseRadius) * i.toFloat(),
                        i * (hatRadius * ratio / baseRadius),
                        colors
                )
            }

            for (i in 0..(hatRadius / ratio).toInt()) {
                colors.setARGB(
                        255,
                        (i * (255 * ratio / hatRadius)).toInt(),
                        (i * (255 * ratio / hatRadius)).toInt(),
                        255
                )
                myCanvas.drawCircle(
                        newX,
                        newY,
                        hatRadius - i.toFloat() * ratio / 2,
                        colors
                ) //Draw the shading for the hat
            }

            holder.unlockCanvasAndPost(myCanvas) //Write the new drawing to the SurfaceView
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        setupDimensions()
        drawJoystick(centerX, centerY)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun onTouch(v: View, e: MotionEvent): Boolean {

        if (v == this) {
            if (e.action != ACTION_UP) {
                val displacement =
                        Math.sqrt(Math.pow((e.x - centerX).toDouble(), 2.0) + Math.pow((e.y - centerY).toDouble(), 2.0))
                                .toFloat()
                if (displacement < baseRadius) {
                    drawJoystick(e.x, e.y)
         //           joystickCallback!!.onJoystickMoved((e.x - centerX) / baseRadius, (e.y - centerY) / baseRadius, id)
                } else {
                    val ratio = baseRadius / displacement
                    val constrainedX = centerX + (e.x - centerX) * ratio
                    val constrainedY = centerY + (e.y - centerY) * ratio
                    drawJoystick(constrainedX, constrainedY)
/*
                    joystickCallback!!.onJoystickMoved(
                            (constrainedX - centerX) / baseRadius,
                            (constrainedY - centerY) / baseRadius,
                            id
                    )
*/
                }
            } else
                drawJoystick(centerX, centerY)

            joystickCallback!!.onJoystickMoved(0f, 0f, id)
        }
        return true
    }

    interface JoystickListener {
        fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int)
    }
}
