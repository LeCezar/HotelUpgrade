package com.lecezar.hotelupgrade.repositories

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.models.Event
import com.lecezar.hotelupgrade.utils.base.FirebaseRepository
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import com.lecezar.hotelupgrade.utils.binding.addSnapshotLifecycleAwareListener

class EventRepository : FirebaseRepository() {
    private var path: String = "/temp"
    private var pathWithUser: String = "/temp"


    init {
        GlobalVariables.currentHotelId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                path = "/Hotels/${(sender as ObservableField<*>).get()}/Events"
            }
        })

        GlobalVariables.currentUserId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pathWithUser = "/Users/${(sender as ObservableField<*>).get()}" + path
            }
        })

        if (!GlobalVariables.currentHotelId.get().isNullOrEmpty()) {
            path = "/Hotels/${GlobalVariables.currentHotelId.get()}/Events"
        }

        if (!GlobalVariables.currentUserId.get().isNullOrEmpty()) {
            pathWithUser = "/Users/${GlobalVariables.currentUserId.get()}" + path
        }
    }

    fun subscribeListenerForAllEvents(
        lifecycleOwner: LifecycleOwner,
        callback: CallbackKt<List<Event>>.() -> Unit
    ) {
        firestore.collection(pathWithUser).addSnapshotLifecycleAwareListener(
            "allEvents", firebaseListenerManager, lifecycleOwner,
            onSuccess = {
                val eventList = mutableListOf<Event>()
                it.documents.forEach { documentSnapshot ->
                    eventList.add(Event.fromDocument(documentSnapshot))
                }
                CallbackKt(callback, eventList)
            },
            onError = {
                it?.also { e ->
                    CallbackKt(callback, e)
                }
            }
        )
    }
}