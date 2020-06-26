package com.lecezar.hotelupgrade.roomsFeature.roomdetails

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.repositories.BookingRepository
import com.lecezar.hotelupgrade.repositories.RoomRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import org.koin.core.KoinComponent
import org.koin.core.inject

class RoomDetailsFragmentVM : BaseViewModel(), KoinComponent {
    private val roomRepository: RoomRepository by inject()
    private val bookingRepository: BookingRepository by inject()

    val selectedRoom = MutableLiveData<Room>()
    val selectedRoomBookings = MutableLiveData<List<Booking>>()
    val selectedRoomPrice = MutableLiveData<String>()


    fun subscribeSelectedRoomListenerWithBookings(id: String, lifecycleOwner: LifecycleOwner) {
        roomRepository.subscribeListenerForOneRoom(id, lifecycleOwner) {
            onSuccess = {
                selectedRoom.value = it
                var price = NO_PRICE_TEXT
                if (it.price != null) {
                    price = it.price.toString()
                }
                selectedRoomPrice.value = price
                bookingRepository.subscribeListenerForBookingsOfARoom(it.id, it.name, lifecycleOwner) {
                    onSuccess = {
                        Log.d("SelectedRoomBookings", it.size.toString())
                        selectedRoomBookings.value = it
                    }
                    onFailure = {

                    }
                }
            }
            onFailure = {

            }
        }
    }

    fun updateRoomPrice(callbackKt: CallbackKt<String>.() -> Unit) {
        val roomId = selectedRoom.value?.id
        val newRoomPrice = selectedRoomPrice.value?.toLong()
        if (newRoomPrice != null && roomId != null) {
            roomRepository.updateRoomPrice(roomId, newRoomPrice, callbackKt)
        }
    }

    companion object {
        const val NO_PRICE_TEXT = "No Price Set"
    }
}