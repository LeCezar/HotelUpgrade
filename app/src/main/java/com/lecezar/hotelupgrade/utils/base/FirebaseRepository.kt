package com.lecezar.hotelupgrade.utils.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.lecezar.hotelupgrade.utils.firebase.FirebaseListenerManager
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class FirebaseRepository : KoinComponent {
    protected val auth: FirebaseAuth = Firebase.auth
    protected val firestore: FirebaseFirestore = Firebase.firestore
    protected val functions: FirebaseFunctions = FirebaseFunctions.getInstance("europe-west3")
    val firebaseListenerManager: FirebaseListenerManager by inject()
}