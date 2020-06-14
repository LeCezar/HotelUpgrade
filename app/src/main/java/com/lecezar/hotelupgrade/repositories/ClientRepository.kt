package com.lecezar.hotelupgrade.repositories

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.firestore.FirebaseFirestoreException
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.models.Client
import com.lecezar.hotelupgrade.utils.base.FirebaseRepository
import com.lecezar.hotelupgrade.utils.binding.CallbackKt
import com.lecezar.hotelupgrade.utils.binding.addSnapshotLifecycleAwareListener

class ClientRepository : FirebaseRepository() {
    private var path: String = "/temp"
    private var pathWithUser: String = "/temp"


    init {
        GlobalVariables.currentHotelId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                path = "/Hotels/${(sender as ObservableField<*>).get()}/Clients"
            }
        })

        GlobalVariables.currentUserId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pathWithUser = "/Users/${(sender as ObservableField<*>).get()}" + path
            }
        })

        if (!GlobalVariables.currentHotelId.get().isNullOrEmpty()) {
            path = "/Hotels/${GlobalVariables.currentHotelId.get()}/Clients"
        }

        if (!GlobalVariables.currentUserId.get().isNullOrEmpty()) {
            pathWithUser = "/Users/${GlobalVariables.currentUserId.get()}" + path
        }
    }

    fun putClient(name: String, phoneNumber: String, callbackKt: CallbackKt<Pair<String, String>>.() -> Unit) {
        firestore.collection(pathWithUser).whereEqualTo("phoneNumber", phoneNumber).get().addOnCompleteListener { task ->
            task.result?.also { snapshot ->
                when {
                    snapshot.documents.size == 0 -> {
                        firestore.collection(pathWithUser).document().apply {
                            set(Client(this.id, name, phoneNumber)).addOnCompleteListener {
                                if (it.isSuccessful)
                                    CallbackKt(callbackKt, this.id to name)
                                else {
                                    CallbackKt(callbackKt, FirebaseFirestoreException("Error adding client", FirebaseFirestoreException.Code.INTERNAL))
                                }
                            }

                        }
                    }
                    snapshot.documents.size == 1 -> {
                        CallbackKt(callbackKt, snapshot.documents[0]["id"].toString() to snapshot.documents[0]["name"].toString())
                    }
                    else -> {
                        CallbackKt(callbackKt, FirebaseFirestoreException("More than 1 of the same client", FirebaseFirestoreException.Code.INTERNAL))
                    }
                }
            }
        }
    }

    fun subscribeListenerForAllClients(
        lifecycleOwner: LifecycleOwner,
        callbackKt: CallbackKt<List<Client>>.() -> Unit
    ) {
        firestore.collection(pathWithUser).addSnapshotLifecycleAwareListener(
            "allClients", firebaseListenerManager, lifecycleOwner,
            onSuccess = { snapshot ->
                val clientList = mutableListOf<Client>()
                snapshot.documents.forEach { docSnapshot ->
                    clientList.add(Client.fromDocument(docSnapshot))
                }
                CallbackKt(callbackKt, clientList)
            },
            onError = { exception ->
                exception?.also {
                    CallbackKt(callbackKt, it)
                }
            }
        )
    }

}