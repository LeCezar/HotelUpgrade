package com.lecezar.hotelupgrade.utils.base


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecycleItemTouchHelper(swipeCallback: (poz: Int) -> Unit) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    var callback: (poz: Int) -> Unit = swipeCallback

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback(viewHolder.adapterPosition)

    }
}