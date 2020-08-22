package com.lecezar.hotelupgrade.utils.binding

import androidx.lifecycle.LifecycleOwner
import com.google.firebase.firestore.*
import com.lecezar.hotelupgrade.utils.firebase.FirebaseListenerManager

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