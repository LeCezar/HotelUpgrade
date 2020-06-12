package com.lecezar.hotelupgrade.chooseHotelFeature

import androidx.recyclerview.widget.DiffUtil
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ItemHotelBinding
import com.lecezar.hotelupgrade.models.Hotel
import com.lecezar.hotelupgrade.utils.base.BaseAdapter

class HotelsAdapter(private val itemClickedCallback: (Hotel) -> Unit) : BaseAdapter<Hotel, ItemHotelBinding>(R.layout.item_hotel, DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Hotel>() {
            override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean = oldItem == newItem
        }
    }

    override fun bind(binding: ItemHotelBinding, item: Hotel, holder: BaseViewHolder<ItemHotelBinding>) {
        binding.itemHotelCardView.setOnClickListener {
            itemClickedCallback.invoke(item)
        }
    }
}