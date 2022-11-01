package com.example.dcom.presentation.common.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseVH(itemView: View): RecyclerView.ViewHolder(itemView) {
    open fun bind(data: Any? = null, position: Int) {}
    open fun bind(data: Any? = null) {}
}
