package com.lecezar.hotelupgrade.utils.binding

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.firestore.*

fun Query.addSnapshotLifecycleAwareListener(
    listenerKey: String,
    firebaseListenerManager: FirebaseListenerManager,
    lifecycleOwner: LifecycleOwner,
    onSuccess: (snapshot: QuerySnapshot) -> Unit,
    onError: (error: FirebaseFirestoreException?) -> Unit = {}
) {
    firebaseListenerManager.addListener(listenerKey, this, lifecycleOwner, onSuccess, onError)
}

fun DocumentReference.addSnapshotLifecycleAwareListener(
    listenerKey: String,
    firebaseListenerManager: FirebaseListenerManager,
    lifecycleOwner: LifecycleOwner,
    onSuccess: (snapshot: DocumentSnapshot) -> Unit,
    onError: (error: FirebaseFirestoreException?) -> Unit = {}
) {
    firebaseListenerManager.addDocumentListener(listenerKey, this, lifecycleOwner, onSuccess, onError)
}


class FirebaseListenerManager {

    private var listenerMap: MutableMap<String, FirebaseLifecycleAwareListener> = hashMapOf()
    private var documentListenerMap: MutableMap<String, FirebaseLifecycleAwareListenerDocument> = hashMapOf()

    /**
     * All listeners subscribed this way should be subscribed inside [Lifecycle.Event.ON_CREATE] of their respective lifecycle owner.
     */
    fun addListener(
        key: String,
        query: Query,
        lifecycleOwner: LifecycleOwner,
        onSuccess: (snapshot: QuerySnapshot) -> Unit,
        onError: (error: FirebaseFirestoreException?) -> Unit = {}
    ) {
        FirebaseLifecycleAwareListener(query, onSuccess, onError).also { listener ->
            lifecycleOwner.lifecycle.addObserver(listener)
            listenerMap.putIfAbsent(key, listener)?.apply {
                this.unsubscribeListener()
                listenerMap[key] = listener
            }
        }
    }

    fun addDocumentListener(
        key: String,
        documentReference: DocumentReference,
        lifecycleOwner: LifecycleOwner,
        onSuccess: (snapshot: DocumentSnapshot) -> Unit,
        onError: (error: FirebaseFirestoreException?) -> Unit = {}
    ) {
        FirebaseLifecycleAwareListenerDocument(documentReference, onSuccess, onError).also { listener ->
            lifecycleOwner.lifecycle.addObserver(listener)
            documentListenerMap.putIfAbsent(key, listener)?.apply {
                this.unsubscribeListener()
                try {
                    lifecycleOwner.lifecycle.removeObserver(this)
                } catch (e: Exception) {

                }
            }
            documentListenerMap[key] = listener
        }
    }


    inner class FirebaseLifecycleAwareListener(
        private val query: Query,
        private val onSuccess: (snapshot: QuerySnapshot) -> Unit,
        private val onError: (error: FirebaseFirestoreException) -> Unit = {}
    ) : LifecycleObserver {
        lateinit var listener: ListenerRegistration
        var listenerSubscribed = false

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun subscribeListener() {
            listener = query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    onError(firebaseFirestoreException)
                } else {
                    if (querySnapshot != null)
                        onSuccess(querySnapshot)
                }
            }
            listenerSubscribed = true
            Log.d("ListenerEvent", "SUBSCRIBED")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun unsubscribeListener() {
            if (listenerSubscribed) {
                listener.remove()
                Log.d("ListenerEvent", "REMOVED")
            } else {
                listenerSubscribed = false
            }
        }
    }


    inner class FirebaseLifecycleAwareListenerDocument(
        private val query: DocumentReference,
        private val onSuccess: (snapshot: DocumentSnapshot) -> Unit,
        private val onError: (error: FirebaseFirestoreException) -> Unit = {}
    ) : LifecycleObserver {
        lateinit var listener: ListenerRegistration
        var listenerSubscribed = false

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun subscribeListener() {
            listener = query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    onError(firebaseFirestoreException)
                } else {
                    if (querySnapshot != null)
                        onSuccess(querySnapshot)
                }
            }
            listenerSubscribed = true
            Log.d("ListenerEvent", "SUBSCRIBED")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun unsubscribeListener() {
            if (listenerSubscribed) {
                listener.remove()
                Log.d("ListenerEvent", "REMOVED")
            } else {
                listenerSubscribed = false
            }
        }
    }
}


class CallbackKt<RESULT_TYPE> {
    var onSuccess: ((result: RESULT_TYPE) -> Unit)? = null
    var onFailure: ((exception: Exception) -> Unit)? = null

    private fun onFailure(exception: Exception) {
        onFailure?.invoke(exception)
    }

    private fun onSuccess(result: RESULT_TYPE) {
        onSuccess?.invoke(result)
    }

    constructor(callBack: CallbackKt<RESULT_TYPE>.() -> Unit, successData: RESULT_TYPE) {
        callBack.invoke(this)
        this.onSuccess(successData)
    }

    constructor(callBack: CallbackKt<RESULT_TYPE>.() -> Unit, exception: Exception) {
        callBack.invoke(this)
        this.onFailure(exception)
    }
}