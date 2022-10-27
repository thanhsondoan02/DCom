package com.example.dcom.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.dcom.R

class ActionBarView @JvmOverloads constructor(
    ctx: Context,
    attributes: AttributeSet
) : RelativeLayout(ctx, attributes) {

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.action_bar_layout, this, true)

    }

}
