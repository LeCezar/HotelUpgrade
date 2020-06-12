package com.lecezar.hotelupgrade.roomsFeature.roomdetails

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.repositories.BookingRepository
import com.lecezar.hotelupgrade.repositories.RoomRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class RoomDetailsFragmentVM : BaseViewModel(), KoinComponent {
    private val roomRepository: RoomRepository by inject()
    private val bookingRepository: BookingRepository by inject()

    val selectedRoom = MutableLiveData<Room>()
    val selectedRoomBookings = MutableLiveData<List<Booking>>()

    fun subscribeSelectedRoomListenerWithBookings(id: String, lifecycleOwner: LifecycleOwner) {
        roomRepository.subscribeListenerForOneRoom(id, lifecycleOwner) {
            onSuccess = {
                selectedRoom.value = it
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

//    fun subscribeListenerForThisRoomBooking(lifecycleOwner: LifecycleOwner) {
//        bookingRepository.subscribeListenerForBookingsOfARoom(roomId, roomName, lifecycleOwner) {
//            onSuccess = {
//                Log.d("SelectedRoomBookings", it.size.toString())
//                selectedRoomBookings.value = it
//            }
//            onFailure = {
//
//            }
//        }
//    }
}