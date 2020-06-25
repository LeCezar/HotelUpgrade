package com.lecezar.hotelupgrade.roomsFeature

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ItemRoomBinding
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.utils.base.BaseAdapter


class RoomsAdapter(val fragmentContext: Context, private val onClickCallback: (Room) -> Unit) :
    BaseAdapter<Room, ItemRoomBinding>(R.layout.item_room, DIFF_CALLBACK) {

    fun getItemAt(position: Int): Room = getItem(position)

    override fun bind(binding: ItemRoomBinding, item: Room, holder: BaseViewHolder<ItemRoomBinding>) {
        if (item.nextBookingInfo == null) {
            binding.itemRoomNextCheckIn.visibility = View.GONE
        } else {
            binding.itemRoomNextCheckInDate.text = item.getFormattedStartBookingDateString(fragmentContext)
            binding.itemRoomNextCheckIn.visibility = View.VISIBLE
        }
        binding.itemRoomRoot.setOnClickListener {
            onClickCallback.invoke(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Room>() {
            override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean = oldItem == newItem
        }
    }
}