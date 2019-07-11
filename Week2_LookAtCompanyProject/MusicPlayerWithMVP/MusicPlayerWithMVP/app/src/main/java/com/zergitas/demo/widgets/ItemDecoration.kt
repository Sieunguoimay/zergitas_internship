package com.zergitas.demo.widgets

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ItemDecoration(
    private var top: Int,
    private var bottom: Int,
    private var left: Int,
    private var right: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = top
        outRect.right = right
        outRect.bottom = bottom
        outRect.left = left
    }
}