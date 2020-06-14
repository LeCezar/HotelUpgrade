package com.lecezar.hotelupgrade.bookingFeature.bookingtabs

import androidx.recyclerview.widget.DiffUtil
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ItemClientBinding
import com.lecezar.hotelupgrade.models.Client
import com.lecezar.hotelupgrade.utils.base.BaseAdapter

class ClientsAdapter : BaseAdapter<Client, ItemClientBinding>(R.layout.item_client, DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Client>() {
            override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean = oldItem == newItem
        }
    }

    override fun bind(binding: ItemClientBinding, item: Client, holder: BaseViewHolder<ItemClientBinding>) {

    }
}