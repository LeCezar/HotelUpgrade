package com.lecezar.hotelupgrade.repositories

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.models.RoomsInterval
import com.lecezar.hotelupgrade.utils.base.FirebaseRepository
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import com.lecezar.hotelupgrade.utils.binding.addSnapshotLifecycleAwareListener
import java.util.*

class RoomRepository : FirebaseRepository() {

    private var pathWithUser: String = "/temp"

    init {
        GlobalVariables.currentHotelId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pathWithUser = "/Users/${GlobalVariables.currentUserId.get()}/Hotels/${(sender as ObservableField<*>).get()}/Rooms"
            }
        })

        GlobalVariables.currentUserId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pathWithUser = "/Users/${(sender as ObservableField<*>).get()}/Hotels/${GlobalVariables.currentHotelId.get()}/Rooms"
            }
        })

        if (!GlobalVariables.currentUserId.get().isNullOrEmpty() && !GlobalVariables.currentHotelId.get().isNullOrEmpty()) {
            pathWithUser = "/Users/${GlobalVariables.currentUserId.get()}/Hotels/${GlobalVariables.currentHotelId.get()}/Rooms"
        }
    }

    fun addSingleRoom(roomNumber: Int, roomFloor: Int?, roomName: String?, price: String?, callback: CallbackKt<String>.() -> Unit) {

        if (auth.currentUser != null) {

            firestore.collection(pathWithUser)
                .document().apply {
                    val newRoom = if (roomName == null) {
                        if (roomFloor != null) {
                            Room("Room ${roomFloor * 100 + roomNumber}", roomNumber, roomFloor, id = this.id, price = price?.toLong())
                        } else {
                            Room("Room $roomNumber", roomNumber, -1, id = this.id, price = price?.toLong())
                        }
                    } else {
                        Room(roomName, roomNumber, roomFloor ?: -1, id = this.id, price = price?.toLong())
                    }
                    set(newRoom.toMap(), SetOptions.merge())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                CallbackKt(callback, "Added room")
                            }
                            if (task.exception != null) {
                                CallbackKt(callback, Exception("Failed adding room"))
                            }
                        }
                }
        } else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }

    fun addMultipleRooms(roomsIntervalList: List<RoomsInterval>, callback: CallbackKt<String>.() -> Unit) {
        if (auth.currentUser != null) {
            roomsIntervalList.forEach { roomInterval ->
                if (roomInterval.startNumber < roomInterval.endNumber) {
                    for (i in roomInterval.startNumber..roomInterval.endNumber) {
                        //try adding room
                        this.addSingleRoom(i, roomInterval.floor, null, null) {
                            onFailure = {
                                //try again in case of failure
                                addSingleRoom(i, roomInterval.floor, null, null) {
                                    onSuccess = {}
                                    //abort adding
                                    onFailure = {
                                        CallbackKt(callback, Exception("Failed | Reverting add ..."))
                                        //undoMultipleAdd(roomsIntervalList)
                                    }
                                }
                                //undoMultipleAdd(roomsIntervalList)
                            };onSuccess = {}
                        }
                    }
                } else
                    CallbackKt(callback, InvalidPropertiesFormatException("The start room number is bigger than end room"))
            }
            CallbackKt(callback, "Added rooms")
        } else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }

    fun subscribeListenerForAllRooms(
        lifecycleOwner: LifecycleOwner,
        callback: CallbackKt<List<Room>>.() -> Unit
    ) {
        if (auth.currentUser != null) {
            firestore.collection(pathWithUser).addSnapshotLifecycleAwareListener("allRooms",
                firebaseListenerManager, lifecycleOwner,
                onSuccess = { snapshot ->
                    val roomList = mutableListOf<Room>()
                    snapshot.documents.forEach { document ->
                        roomList.add(Room.fromDocument(document))
                    }
                    CallbackKt(callback, roomList)
                },
                onError = { e ->
                    if (e != null) {
                        CallbackKt(callback, e)
                    }
                }
            )


        } else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }

    fun subscribeListenerForOneRoom(
        id: String, lifecycleOwner: LifecycleOwner,
        callback: CallbackKt<Room>.() -> Unit
    ) {
        if (auth.currentUser != null) {
            firestore.document("$pathWithUser/$id").addSnapshotLifecycleAwareListener(
                id, firebaseListenerManager, lifecycleOwner,
                onSuccess = { document ->
                    CallbackKt(callback, Room.fromDocument(document))
                },
                onError = { e ->
                    if (e != null) {
                        CallbackKt(callback, e)
                    }
                }

            )
        } else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }

    fun updateRoom(
        roomId: String,
        name: String? = null,
        number: Int? = null,
        floor: Int? = null,
        occupationStatus: Room.Companion.OccupationStatus? = null,
        cleaningStatus: Room.Companion.CleaningStatus? = null,
        nextBookingInfo: Pair<String, Date>? = null,
        callback: CallbackKt<String>.() -> Unit
    ) {
        if (auth.currentUser != null) {
            val updateMap = mutableMapOf<String, Any>()

            if (name != null) {
                updateMap["name"] = name
            }
            if (number != null) {
                updateMap["name"] = number
            }
            if (floor != null) {
                updateMap["floor"] = floor
            }
            if (occupationStatus != null) {
                updateMap["occupationStatus"] = occupationStatus
            }
            if (cleaningStatus != null) {
                updateMap["cleaningStatus"] = cleaningStatus
            }
            if (nextBookingInfo != null) {
                updateMap["nextBookingInfo"] = mapOf(nextBookingInfo.first to Timestamp(nextBookingInfo.second))
            }
            firestore.collection(pathWithUser).document(roomId).set(updateMap, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful) {
                    CallbackKt(callback, "Update successful!")
                }
                it.exception?.apply {
                    CallbackKt(callback, this)
                }
            }
        } else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }

    fun updateRoomStatuses(
        callback: CallbackKt<String>.() -> Unit
    ) {
        val currentTimestamp = Timestamp(Calendar.getInstance().time)
        val data = mapOf(
            "hotelId" to GlobalVariables.currentHotelId.get(),
            "currentSeconds" to currentTimestamp.seconds,
            "currentNano" to currentTimestamp.nanoseconds
        )
        functions.getHttpsCallable("updateRoomsStatuses").call(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.also { resultNotNull ->
                    val resultMap = resultNotNull.data as Map<*, *>
                    CallbackKt(callback, resultMap["roomStatusFunctionResult"]?.toString() ?: "No result")
                }
            } else {
                task.exception?.also {
                    CallbackKt(callback, it)
                }
            }
        }
    }

    fun deleteRoom(roomId: String, callback: CallbackKt<String>.() -> Unit) {
        firestore.collection(pathWithUser).document("/$roomId").delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                CallbackKt(callback, "Deleted Room")
            }
            task.exception?.apply {
                CallbackKt(callback, this)
            }
        }
    }

//    private fun undoMultipleAdd(roomsIntervalList: List<RoomsInterval>) {
//        //todo
//    }
}