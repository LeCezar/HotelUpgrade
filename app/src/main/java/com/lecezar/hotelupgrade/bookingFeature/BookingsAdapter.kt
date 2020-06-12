package com.lecezar.hotelupgrade.bookingFeature

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.bookingFeature.bookingtabs.ItemRoomSmallAdapter
import com.lecezar.hotelupgrade.databinding.ItemBookingBinding
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.utils.base.BaseAdapter
import com.lecezar.hotelupgrade.utils.format_mm_DD_YY

class BookingsAdapter(private val fragmentContext: Context, private val onItemRoomSmallClickCallback: (roomId: String) -> Unit) :
    BaseAdapter<Booking, ItemBookingBinding>(R.layout.item_booking, DIFF_CALLBACK) {


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Booking>() {
            override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean = oldItem == newItem
        }
    }

    override fun bind(binding: ItemBookingBinding, item: Booking, holder: BaseViewHolder<ItemBookingBinding>) {
        binding.itemBookingStart.text = item.startDate.format_mm_DD_YY(fragmentContext)
        binding.itemBookingEnd.text = item.endDate.format_mm_DD_YY(fragmentContext)
        binding.itemBookingItemRoomSmallRecycler.apply {
            this.layoutManager = LinearLayoutManager(fragmentContext, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = ItemRoomSmallAdapter {
                this@BookingsAdapter.onItemRoomSmallClickCallback.invoke(it)
            }
            (this.adapter as ItemRoomSmallAdapter).submitList(item.rooms.toList())
        }
    }
}