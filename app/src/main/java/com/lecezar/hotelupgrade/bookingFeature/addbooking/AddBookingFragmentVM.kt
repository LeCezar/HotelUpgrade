package com.lecezar.hotelupgrade.bookingFeature.addbooking

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.repositories.BookingRepository
import com.lecezar.hotelupgrade.repositories.ClientRepository
import com.lecezar.hotelupgrade.repositories.RoomRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.security.InvalidParameterException
import java.util.*

class AddBookingFragmentVM : BaseViewModel(), KoinComponent {
    private val bookingRepository: BookingRepository by inject()
    private val clientRepository: ClientRepository by inject()
    private val roomRepository: RoomRepository by inject()

    val selectedRoomsList: MutableList<Room> = mutableListOf()
    val phoneNumber = MutableLiveData<String>()
    val clientName = MutableLiveData<String>()
    val clientNameErrorState = clientName.map {
        return@map it.isNullOrBlank()
    }
    val phoneNumberErrorState = phoneNumber.map {
        return@map it.isNullOrBlank()
    }
    val availableRoomsList = MutableLiveData<List<Room>>()
    val startDate = MutableLiveData<Date>()
    val endDate = MutableLiveData<Date>()
    val dateErrorState = endDate.map {
        startDate.value?.also { start ->
            endDate.value?.also { end ->
                return@map start >= end
            }
            return@map true
        }
        return@map true
    }

    fun fetchAvailableRoomsList() {
        if (dateErrorState.value == false) {
            bookingRepository.getRoomsAvailableInTimeFrame(startDate.value!!, endDate.value!!) {
                onSuccess = {
                    availableRoomsList.value = it
                }
                onFailure = {
                    Log.d("AvailableRooms", it.message ?: "No message")
                }
            }
        }
    }

    fun addBooking() {
        if (clientNameErrorState.value == false && phoneNumberErrorState.value == false)
            clientRepository.putClient(clientName.value!!, phoneNumber.value!!) {
                onSuccess = {
                    if (dateErrorState.value == false) {
                        bookingRepository.addBooking(it, startDate.value!!, endDate.value!!, selectedRoomsList) {
                            onSuccess = {

                            }
                            onFailure = {

                            }
                        }
                    }
                }
            }
    }

    fun addBooking(callbackKt: CallbackKt<String>.() -> Unit) {
        if (dateErrorState.value == false) {
            if (clientNameErrorState.value == false && phoneNumberErrorState.value == false) {
                clientRepository.putClient(clientName.value!!, phoneNumber.value!!) {
                    onSuccess = { clientIdName ->
                        bookingRepository.addBooking(clientIdName, startDate.value!!, endDate.value!!, selectedRoomsList) {
                            onSuccess = { bookingId ->
                                roomRepository.updateRoomStatuses { onSuccess = {}; onFailure = {} }
                                selectedRoomsList.removeIf { true }
                                CallbackKt(callbackKt, "Added booking!")
                            }
                            onFailure = { addBookingException ->
                                CallbackKt(callbackKt, addBookingException)
                            }
                        }
                    }
                }
            } else {
                CallbackKt(callbackKt, InvalidParameterException("Client Error"))
            }
        } else {
            CallbackKt(callbackKt, InvalidParameterException("Date error"))
        }
    }


    fun selectRoom(room: Room) {
        selectedRoomsList.add(room)
    }

    fun unSelectRoom(room: Room) {
        selectedRoomsList.remove(room)
    }

}