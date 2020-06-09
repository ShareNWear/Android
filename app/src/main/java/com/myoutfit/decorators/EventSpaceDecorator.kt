package com.myoutfit.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EventSpaceDecorator(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = (view.layoutParams as? RecyclerView.LayoutParams)?.viewAdapterPosition
        outRect.left = 0
        outRect.right = 0
        outRect.bottom = if (state.itemCount - 1 == itemPosition) margin else 0
        outRect.top = if (itemPosition == 0) margin else 0
    }
}