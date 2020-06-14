package com.lecezar.hotelupgrade.utils.base


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class RecycleItemTouchHelper(swipeCallback: (poz: Int) -> Unit): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

    var callback: (poz: Int) -> Unit = swipeCallback

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback(viewHolder.adapterPosition)
        Snackbar.make(viewHolder.itemView,"DELETED",Snackbar.LENGTH_SHORT).show()
    }
}