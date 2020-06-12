package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import androidx.recyclerview.widget.DiffUtil
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ItemRoomSmallBinding
import com.lecezar.hotelupgrade.utils.base.BaseAdapter

class ItemRoomSmallAdapter(val onClick: (roomId: String) -> Unit) : BaseAdapter<Pair<String, String>, ItemRoomSmallBinding>(
    R.layout.item_room_small,
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Pair<String, String>>() {
            override fun areItemsTheSame(oldItem: Pair<String, String>, newItem: Pair<String, String>): Boolean = oldItem.first == newItem.first
            override fun areContentsTheSame(oldItem: Pair<String, String>, newItem: Pair<String, String>): Boolean = oldItem == newItem
        }
    }

    override fun bind(binding: ItemRoomSmallBinding, item: Pair<String, String>, holder: BaseViewHolder<ItemRoomSmallBinding>) {
        binding.itemSmallRoomRoot.setOnClickListener {
            onClick.invoke(item.first)
        }
    }
}