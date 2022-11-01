package com.example.dcom.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class QuarterCircle(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    companion object {
        const val CIRCLE_COLOR = "#415e9b"
        const val CIRCLE_STROKE_COLOR = "#a2afc9"
    }

    private var mDrawPaint: Paint? = null

    init {
        initPaint()
    }

    private fun initPaint() {
        mDrawPaint = Paint()
        mDrawPaint!!.color = Color.parseColor(CIRCLE_COLOR)
        mDrawPaint!!.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(width.toFloat(), 0f, width.toFloat(), mDrawPaint!!.apply { color = Color.parseColor(
            CIRCLE_STROKE_COLOR
        ) })
        canvas.drawCircle(width.toFloat(), 0f, width.toFloat() * 8 / 10, mDrawPaint!!.apply { color = Color.parseColor(
            CIRCLE_COLOR
        ) })
    }

}

