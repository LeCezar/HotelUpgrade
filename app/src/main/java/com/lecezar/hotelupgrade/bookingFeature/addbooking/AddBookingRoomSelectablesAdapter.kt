package com.lecezar.hotelupgrade.bookingFeature.addbooking

import androidx.recyclerview.widget.DiffUtil
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.ItemRoomSelectableBinding
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.utils.base.BaseAdapter

class AddBookingRoomSelectablesAdapter(
    private val onRoomCheckBoxPressedListener: (checked: Boolean, room: Room) -> Unit,
    private val onRoomImagePressedListener: (room: Room) -> Unit
) : BaseAdapter<Room, ItemRoomSelectableBinding>(R.layout.item_room_selectable, DIFF_CALLBACK) {

    private val itemsMapCheckedStatus = mutableMapOf<String, Boolean>()

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Room>() {
            override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean = oldItem == newItem
        }
    }

    override fun bind(binding: ItemRoomSelectableBinding, item: Room, holder: BaseViewHolder<ItemRoomSelectableBinding>) {
        binding.itemRoomSelectableCheckBox.isChecked = itemsMapCheckedStatus[item.id] ?: false
        binding.itemRoomSelectableCheckBox.setOnCheckedChangeListener { _, b ->
            itemsMapCheckedStatus[item.id] = b
            onRoomCheckBoxPressedListener.invoke(b, item)
        }
        binding.itemRoomSelectableImage.setOnClickListener {
            onRoomImagePressedListener.invoke(item)
        }
    }

//    private fun switchToCheckedView(binding: ItemRoomSelectableBinding) {
//    }
//
//    private fun switchToUncheckedView(binding: ItemRoomSelectableBinding) {
//
//    }

}