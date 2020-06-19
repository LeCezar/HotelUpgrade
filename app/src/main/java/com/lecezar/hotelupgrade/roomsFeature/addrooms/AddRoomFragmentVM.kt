package com.lecezar.hotelupgrade.roomsFeature.addrooms

import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.RoomsInterval
import com.lecezar.hotelupgrade.repositories.RoomRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import org.koin.core.KoinComponent
import org.koin.core.inject

class AddRoomFragmentVM : BaseViewModel(), KoinComponent {

    private val roomRepository: RoomRepository by inject()

    val roomsEndNumber = MutableLiveData<String>()
    val roomsStartNumber = MutableLiveData<String>()
    val floorNumber = MutableLiveData<String>()
    val singleRoomFloor = MutableLiveData<String>()
    val singleRoomName = MutableLiveData<String>()
    val singleRoomNumber = MutableLiveData<String>()
    val roomsIntervalsSelected = mutableListOf<RoomsInterval>()
    val price = MutableLiveData<String>()

    fun addSingleRoom(onResult: CallbackKt<String>.() -> Unit) {
        singleRoomNumber.value?.apply {
            roomRepository.addSingleRoom(this.toInt(), singleRoomFloor.value?.toInt(), singleRoomName.value, price.value, onResult)
        }
    }

    fun addMultipleRooms(callback: CallbackKt<String>.() -> Unit) {
        roomRepository.addMultipleRooms(roomsIntervalsSelected, callback)
    }

    //TODO bug when writing something and then deleting will cause app to crash
    // see floor.toInt()

    fun saveCurrentSelectedRoomsInterval(): String {
        if (roomsStartNumber.value?.toInt() ?: Int.MAX_VALUE < roomsEndNumber.value?.toInt() ?: -1) {
            roomsStartNumber.value?.also { start ->
                roomsEndNumber.value?.also { end ->
                    RoomsInterval(start.toInt(), end.toInt(), floorNumber.value?.toInt()).apply {
                        roomsIntervalsSelected.add(this)
                        return this.toString()
                    }
                }
            }
        }
        return ""
    }

}