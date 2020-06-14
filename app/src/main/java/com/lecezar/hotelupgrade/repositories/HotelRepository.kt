package com.lecezar.hotelupgrade.repositories

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.models.Hotel
import com.lecezar.hotelupgrade.utils.base.FirebaseRepository
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import com.lecezar.hotelupgrade.utils.binding.addSnapshotLifecycleAwareListener

/**
 * @param pathWithUser should lead to the hotels collection, not to specific collections
 */
class HotelRepository() : FirebaseRepository() {

    private var pathWithUser: String = "/temp"

    init {

        GlobalVariables.currentUserId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pathWithUser = "/Users/${(sender as ObservableField<*>).get()}/Hotels"
            }
        })

        if (!GlobalVariables.currentUserId.get().isNullOrEmpty()) {
            pathWithUser = "/Users/${GlobalVariables.currentUserId.get()}/Hotels"
        }
    }


    fun subscribeListenerForAllHotels(
        lifecycleOwner: LifecycleOwner,
        callback: CallbackKt<List<Hotel>>.() -> Unit
    ) {
        if (auth.currentUser != null)
            firestore.collection(pathWithUser).addSnapshotLifecycleAwareListener("allHotels",
                firebaseListenerManager, lifecycleOwner,
                onSuccess = { snapshot ->
                    val hotelList = mutableListOf<Hotel>()
                    snapshot.documents.forEach { document ->
                        hotelList.add(Hotel(document.id, document["Name"].toString()))
                    }
                    CallbackKt(callback, hotelList)
                }, onError = { e ->
                    if (e != null) {
                        CallbackKt(callback, e)
                        Log.e("FirebaseError", e.message ?: "No message provided")
                    }
                })
        else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }

    fun getAllHotels(
        callback: CallbackKt<List<Hotel>>.() -> Unit
    ) {
        if (auth.currentUser != null) {
            firestore.collection(pathWithUser).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val hotelList = mutableListOf<Hotel>()
                    task.result?.documents?.forEach { document ->
                        hotelList.add(Hotel(document["id"].toString(), document["name"].toString()))
                    }
                    CallbackKt(callback, hotelList)
                } else {
                    task.exception?.also { exception ->
                        CallbackKt(callback, exception)
                    }
                }
            }
        } else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }

    fun addHotel(
        hotelName: String,
        callback: CallbackKt<String>.() -> Unit
    ) {
        if (auth.currentUser != null) {
            firestore.collection(pathWithUser).document().apply {
                set(
                    mapOf(
                        "id" to this.id,
                        "name" to hotelName
                    )
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        CallbackKt(callback, "Success adding $hotelName")

                    } else {
                        it.exception?.also { exception ->
                            CallbackKt(callback, exception)
                        }
                    }
                }
            }

        } else {
            CallbackKt(callback, NoSuchFieldException("No user"))
        }
    }
}

