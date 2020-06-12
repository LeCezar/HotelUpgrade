package com.lecezar.hotelupgrade.eventsFeature

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.bookingFeature.bookingtabs.ItemRoomSmallAdapter
import com.lecezar.hotelupgrade.databinding.ItemEventBinding
import com.lecezar.hotelupgrade.models.Event
import com.lecezar.hotelupgrade.utils.base.BaseAdapter

class EventsAdapter(val fragmentContext: Context, val onItemRoomSmallClickCallback: (roomId: String) -> Unit) :
    BaseAdapter<Event, ItemEventBinding>(R.layout.item_event, DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.eventId == newItem.eventId
            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
        }
    }

    override fun bind(binding: ItemEventBinding, item: Event, holder: BaseViewHolder<ItemEventBinding>) {
        binding.itemEventsRoomSmallRecyclerView.apply {
            this.layoutManager = GridLayoutManager(fragmentContext, 2)
            this.adapter = ItemRoomSmallAdapter {
                this@EventsAdapter.onItemRoomSmallClickCallback.invoke(it)
            }
            (this.adapter as ItemRoomSmallAdapter).submitList(item.roomsAffected.toList())
        }
        when (item.eventType) {
            Event.Companion.EventType.CHECKOUT -> binding.itemEventIcon.setImageDrawable(fragmentContext.getDrawable(R.drawable.ic_flight_takeoff))
            Event.Companion.EventType.CHECKIN -> binding.itemEventIcon.setImageDrawable(fragmentContext.getDrawable(R.drawable.ic_flight_land))
        }

    }
}