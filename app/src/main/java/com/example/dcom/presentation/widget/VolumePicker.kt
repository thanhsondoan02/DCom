package com.example.dcom.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class VolumePicker(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    companion object {
        const val SMALL_CIRCLE_RADIUS = 17f
        const val BIG_CIRCLE_RADIUS = 25f
        const val TOUCH_RANGE = 50f
    }

    private var mDrawPaint: Paint? = null

    // Set default values
    private var mStrokeWidth = 10
//    private var paintColor = "#21E8AC"
    private var paintColor = "#1BA0E2"
    private var circleRadius = SMALL_CIRCLE_RADIUS
    private var circleCenterX: Float? = null
    private var circleCenterY: Float? = null
    private var lineWidth: Float? = null

    private var minPoint: Float? = null
    private var maxPoint: Float? = null
    private var verticalPoint: Float? = null

    init {
        initPaint()
    }

    private fun initPaint() {
        mDrawPaint = Paint()
        mDrawPaint!!.color = Color.parseColor(paintColor)
        mDrawPaint!!.isAntiAlias = true
        mDrawPaint!!.strokeWidth = mStrokeWidth.toFloat()
//        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        if (minPoint == null) minPoint = BIG_CIRCLE_RADIUS
        if (maxPoint == null) maxPoint = width - BIG_CIRCLE_RADIUS
        if (verticalPoint == null) verticalPoint = height / 2f


        if (lineWidth == null) lineWidth = maxPoint!!
        canvas.drawLine(minPoint!!, verticalPoint!!, lineWidth!!, verticalPoint!!, mDrawPaint!!)

        if (circleCenterX == null) circleCenterX = maxPoint
        if (circleCenterY == null) circleCenterY = verticalPoint
        canvas.drawCircle(circleCenterX!!, circleCenterY!!, circleRadius, mDrawPaint!!)
    }

    var isMoving = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isTouchInCircle(touchX, touchY)) {
                    isMoving = true
                    circleRadius = BIG_CIRCLE_RADIUS
                } else if (touchY < circleCenterY!! + TOUCH_RANGE && touchY > circleCenterY!! - TOUCH_RANGE) {
                    moveToTouchX(touchX)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isMoving) {
                    moveToTouchX(touchX)
                }
            }
            MotionEvent.ACTION_UP -> {
                circleRadius = SMALL_CIRCLE_RADIUS
                isMoving = false
            }
            else -> return false
        }

        invalidate()
        return true
    }

    private fun isTouchXInRange(touchX: Float) : Boolean {
        return touchX >= minPoint!! && touchX <= maxPoint!!
    }

    private fun moveToTouchX(touchX: Float) {
        if (isTouchXInRange(touchX)) {
            circleCenterX = touchX
            lineWidth = touchX
        }
    }

    private fun isTouchInCircle(touchX: Float, touchY: Float): Boolean {
        val dx = touchX - circleCenterX!!
        val dy = touchY - circleCenterY!!
        val distance = sqrt((dx * dx + dy * dy).toDouble())
        return distance < TOUCH_RANGE
    }


    fun setPickerLength(percent: Int) {
        if (percent in 0..100) {
            lineWidth = maxPoint!! * percent / 100
            circleCenterX = lineWidth
            invalidate()
        } else {
            throw IllegalArgumentException("Percent must be in range 0..100")
        }
    }

}

