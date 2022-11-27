package com.example.dcom.presentation.widget

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class CustomGridLayoutManager {

    private var maxItemHorizontal: Int = 2
    private var orientation: Int = RecyclerView.VERTICAL

    abstract fun getMaxItemHorizontalByViewType(viewType: Int): Int
    abstract fun getItemViewType(adapterPosition: Int): Int

    fun getGridLayoutManager(context: Context): RecyclerView.LayoutManager {
        val spanCount = getOptimalSpanCount(maxItemHorizontal)
        return GridLayoutManager(context, spanCount, orientation, false).apply {
            this.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return getItemSpanSize(position, spanCount)
                }
            }
        }
    }

    private fun getOptimalSpanCount(maxItemHorizontal: Int): Int {
        var spanCount = 1
        if (maxItemHorizontal <= 0) {
            return 0
        }
        for (i in 1..maxItemHorizontal) {
            spanCount *= maxItemHorizontal
        }
        return spanCount
    }

//    private fun getMaxItemHorizontalByViewType(viewType: Int): Int {
//        return when (viewType) {
//            FavoriteAdapter.SEARCH -> 1
//            FavoriteAdapter.NOTE -> 2
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
//    }

    private fun getItemSpanSize(adapterPosition: Int, spanCount: Int): Int {
        val viewType = getItemViewType(adapterPosition)
        val maxItemHorizontal = getMaxItemHorizontalByViewType(viewType)
        return spanCount / maxItemHorizontal
    }

}
