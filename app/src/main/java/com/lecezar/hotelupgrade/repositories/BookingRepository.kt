package com.lecezar.hotelupgrade.repositories

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.Timestamp
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.models.Booking
import com.lecezar.hotelupgrade.models.Room
import com.lecezar.hotelupgrade.utils.base.FirebaseRepository
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import com.lecezar.hotelupgrade.utils.binding.addSnapshotLifecycleAwareListener
import java.util.*

class BookingRepository : FirebaseRepository() {
    private var pathWithUser: String = "/temp"
    private var roomsPathWithUser: String = "/temp"

    init {
        GlobalVariables.currentHotelId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pathWithUser = "/Users/${GlobalVariables.currentUserId.get()}/Hotels/${(sender as ObservableField<*>).get()}/Bookings"
                roomsPathWithUser = "/Users/${GlobalVariables.currentUserId.get()}/Hotels/${(sender as ObservableField<*>).get()}/Rooms"
            }
        })

        GlobalVariables.currentUserId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                roomsPathWithUser = "/Users/${(sender as ObservableField<*>).get()}/Hotels/${GlobalVariables.currentHotelId.get()}/Bookings"
                roomsPathWithUser = "/Users/${(sender as ObservableField<*>).get()}/Hotels/${GlobalVariables.currentHotelId.get()}/Rooms"
            }
        })

        if (!GlobalVariables.currentHotelId.get().isNullOrEmpty() && !GlobalVariables.currentUserId.get().isNullOrEmpty()) {
            pathWithUser = "/Users/${GlobalVariables.currentUserId.get()}/Hotels/${GlobalVariables.currentHotelId.get()}/Bookings"
            roomsPathWithUser = "/Users/${GlobalVariables.currentUserId.get()}/Hotels/${GlobalVariables.currentHotelId.get()}/Rooms"
        }
    }

    fun subscribeListenerForUpcomingBookings(
        lifecycleOwner: LifecycleOwner,
        callback: CallbackKt<List<Booking>>.() -> Unit
    ) {
        if (auth.currentUser != null) {
            firestore.collection(pathWithUser).whereGreaterThanOrEqualTo("startDate", Timestamp(Calendar.getInstance().time))
                .addSnapshotLifecycleAwareListener("allBookings",
                    firebaseListenerManager, lifecycleOwner,
                    onSuccess = { snapshot ->
                        val bookingList = mutableListOf<Booking>()
                        snapshot.documents.forEach { document ->
                            bookingList.add(Booking.fromDocument(document))
                        }
                        CallbackKt(callback, bookingList)
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

    fun subscribeListenerForBookingsOfARoom(
        roomId: String,
        roomName: String,
        lifecycleOwner: LifecycleOwner,
        callback: CallbackKt<List<Booking>>.() -> Unit
    ) {
        firestore.collection(pathWithUser).whereEqualTo("rooms.$roomId", roomName).addSnapshotLifecycleAwareListener(
            roomId, firebaseListenerManager, lifecycleOwner,
            onSuccess = { querySnapshot ->
                val bookingsList = mutableListOf<Booking>()
                querySnapshot.documents.forEach { document ->
                    bookingsList.add(Booking.fromDocument(document))
                }
                CallbackKt(callback, bookingsList)
            },
            onError = {
                it?.also {
                    CallbackKt(callback, it)
                }
            }
        )
    }

    fun subscribeListenerForPastBookings(
        lifecycleOwner: LifecycleOwner,
        callback: CallbackKt<List<Booking>>.() -> Unit
    ) {
        if (auth.currentUser != null) {
            firestore.collection(pathWithUser).whereLessThan("startDate", Timestamp(Calendar.getInstance().time))
                .addSnapshotLifecycleAwareListener("allBookings",
                    firebaseListenerManager, lifecycleOwner,
                    onSuccess = { snapshot ->
                        val bookingList = mutableListOf<Booking>()
                        snapshot.documents.forEach { document ->
                            bookingList.add(Booking.fromDocument(document))
                        }
                        CallbackKt(callback, bookingList)
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

    fun getRoomsAvailableInTimeFrame(
        startDate: Date,
        endDate: Date,
        callback: CallbackKt<List<Room>>.() -> Unit
    ) {
        firestore.collection(pathWithUser)
            .whereGreaterThan("endDate", Timestamp(startDate))
            .get()
            .addOnCompleteListener { bookingTask ->
                if (bookingTask.isSuccessful) {
                    bookingTask.result?.also { result ->
                        val bookingList = mutableListOf<Booking>()
                        result.documents.forEach {
                            bookingList.add(Booking.fromDocument(it))
                        }
                        val bookingListFiltered = filterBookingsWhereStartLessThan(bookingList, endDate)
                        val filteredRoomsList = mutableListOf<Room>()
                        val bookedRoomsIdSet: MutableSet<String> = mutableSetOf()
                        bookingListFiltered.forEach { booking ->
                            booking.rooms.forEach { roomEntry ->
                                bookedRoomsIdSet.add(roomEntry.key)
                            }
                        }
                        firestore.collection(roomsPathWithUser).get().addOnCompleteListener { roomsTask ->
                            if (roomsTask.isSuccessful) {
                                roomsTask.result?.documents?.forEach { documentSnapshot ->
                                    documentSnapshot["id"]?.also { roomId ->
                                        if ((roomId as String) !in bookedRoomsIdSet) {
                                            filteredRoomsList.add(Room.fromDocument(documentSnapshot))
                                        }
                                    }
                                }
                                CallbackKt(callback, filteredRoomsList)
                            }
                            roomsTask.exception?.also { exception ->
                                Log.d("FirestoreLogs", exception.message ?: "Null message")
                                Log.d("FirestoreLogs", exception.localizedMessage?.toString() ?: "Null LOC_MESSAGE")
                                CallbackKt(callback, exception)
                            }
                        }
                    }
                }
                bookingTask.exception?.also { exception ->
                    Log.d("FirestoreLogs", exception.message ?: "Null message")
                    Log.d("FirestoreLogs", exception.localizedMessage?.toString() ?: "Null LOC_MESSAGE")
                    CallbackKt(callback, exception)
                }
            }
    }

    fun addBooking(
        clientIdName: Pair<String, String>,
        startDate: Date,
        endDate: Date,
        selectedRooms: List<Room>,
        callback: CallbackKt<String>.() -> Unit
    ) {
        GlobalVariables.currentHotelId.get()?.also {
            firestore.collection(pathWithUser).document().apply {
                val idNameMap = roomsListToIdNameMap(selectedRooms)
                set(Booking(this.id, clientIdName, startDate, endDate, idNameMap).toMap()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        CallbackKt(callback, this.id)
                    } else {
                        it.exception?.also { e ->
                            CallbackKt(callback, e)
                        }
                    }
                }
            }
        }
    }

    fun deleteBooking(bookingId: String, callback: CallbackKt<String>.() -> Unit) {
        firestore.collection(pathWithUser).document("/$bookingId").delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                CallbackKt(callback, "Deleted Booking")
            }
            task.exception?.also { e ->
                CallbackKt(callback, e)
            }
        }
    }

    private fun roomsListToIdNameMap(selectedRooms: List<Room>): Map<String, String> {
        val idNameMap: MutableMap<String, String> = mutableMapOf()
        selectedRooms.forEach {
            idNameMap[it.id] = it.name
        }
        return idNameMap
    }

    private fun filterBookingsWhereStartLessThan(list: List<Booking>, value: Date): List<Booking> {
        return list.filter { booking ->
            booking.startDate < value
        }
    }
}