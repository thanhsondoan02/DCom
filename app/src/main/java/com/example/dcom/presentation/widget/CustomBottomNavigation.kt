package com.example.dcom.presentation.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import com.example.dcom.R

class CustomBottomNavigation @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr) {

    interface IListener {
        fun onClickItem(index: Int)
    }

    var listener: IListener? = null

    private var cvItem0: CardView
    private var cvItem1: CardView
    private var cvItem2: CardView
    private var cvItem3: CardView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_bottom_navigation_layout, this, true)
        cvItem0 = view.findViewById(R.id.cvCustomBottomNavigationItem0)
        cvItem1 = view.findViewById(R.id.cvCustomBottomNavigationItem1)
        cvItem2 = view.findViewById(R.id.cvCustomBottomNavigationItem2)
        cvItem3 = view.findViewById(R.id.cvCustomBottomNavigationItem3)

        setOnClickItem()
    }

    private fun setOnClickItem() {
        cvItem0.setOnClickListener {
            setActiveItem(0)
            listener?.onClickItem(0)
        }
        cvItem1.setOnClickListener {
            setActiveItem(1)
            listener?.onClickItem(1)
        }
        cvItem2.setOnClickListener {
            setActiveItem(2)
            listener?.onClickItem(2)
        }
        cvItem3.setOnClickListener {
            setActiveItem(3)
            listener?.onClickItem(3)
        }
    }

    private fun setActiveItem(index: Int) {
        cvItem0.setBackgroundColor(Color.parseColor("#00000000"))
        cvItem1.setBackgroundColor(Color.parseColor("#00000000"))
        cvItem2.setBackgroundColor(Color.parseColor("#00000000"))
        cvItem3.setBackgroundColor(Color.parseColor("#00000000"))

        when (index) {
            0 -> cvItem0.setBackgroundColor(Color.parseColor("#FF416C"))
            1 -> cvItem1.setBackgroundColor(Color.parseColor("#FF416C"))
            2 -> cvItem2.setBackgroundColor(Color.parseColor("#FF416C"))
            3 -> cvItem3.setBackgroundColor(Color.parseColor("#FF416C"))
            else -> throw IllegalArgumentException()
        }
    }

}
