package com.lecezar.hotelupgrade.roomsFeature

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.repositories.RoomRepository
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import org.koin.core.KoinComponent
import org.koin.core.inject

class RoomsFragmentVM : BaseViewModel(), KoinComponent {
    private val roomRepository: RoomRepository by inject()
    val roomList = MutableLiveData<List<Room>>()

    fun subscribeListenerForAllRooms(lifecycleOwner: LifecycleOwner) {
        roomRepository.subscribeListenerForAllRooms(lifecycleOwner) {
            onSuccess = {
                roomList.value = it
            }
            onFailure = {

            }
        }
    }

    fun updateRoomStatuses() {
        roomRepository.updateRoomStatuses {
            onSuccess = {
                Log.d("StatusUpdates", it)
            }
            onFailure = {
                Log.d("StatusUpdatesF", it.message ?: it.localizedMessage ?: "No message")
            }
        }
    }

    fun deleteRoom(roomId: String, callbackKt: CallbackKt<String>.() -> Unit) {
        roomRepository.deleteRoom(roomId, callbackKt)
    }
}